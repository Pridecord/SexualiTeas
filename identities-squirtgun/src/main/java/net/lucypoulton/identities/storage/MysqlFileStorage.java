/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of Identities.
 *
 * Identities is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Identities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Identities.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lucypoulton.identities.storage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.zaxxer.hikari.HikariDataSource;
import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.config.SqlInfoContainer;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.scheduler.Task;
import net.lucypoulton.squirtgun.util.UuidUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class MysqlFileStorage implements Storage {

    private final HikariDataSource ds = new HikariDataSource();
    private final Identities plugin;
    private final Multimap<UUID, String> cache = ArrayListMultimap.create();

    public MysqlFileStorage(Identities plugin) throws MysqlConnectionException {
        this.plugin = plugin;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getPlatform().getLogger().severe("MySQL driver not found! Unable to continue!");
            throw new MysqlConnectionException();
        }
        SqlInfoContainer sqlData = plugin.getConfigHandler().getSqlConnectionData();

        ds.setJdbcUrl("jdbc:mysql://" + sqlData.getHost() + ":" + sqlData.getPort() + "/"
            + sqlData.getDatabase() + "?useSSL=false");
        ds.setUsername(sqlData.getUsername());
        ds.setPassword(sqlData.getPassword());
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts ", "true");

        try (Connection connection = ds.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS identities_players ( playerUuid VARCHAR(36), identities TEXT, priority INT )");

            // ensure priority column is present
            ResultSet table = connection.prepareStatement("SHOW COLUMNS FROM identitys_players WHERE FIELD='priority'").executeQuery();
            if (!table.next()) {
                connection.createStatement().execute("ALTER TABLE identitys_players ADD priority INT DEFAULT 0");
                plugin.getPlatform().getLogger().warning("Migrated MySQL schema (added priority column).");
            }

            plugin.getPlatform().getLogger().info("Connected to MySQL.");
        } catch (SQLException e) {
            plugin.getPlatform().getLogger().severe("Failed to connect to MySQL! - " + e);
            throw new MysqlConnectionException();
        }

    }

    public void onPlayerDisconnect(UUID uuid) {
        cache.removeAll(uuid);
    }

    @Override
    public Set<String> getIdentities(UUID uuid) {
        return getIdentities(uuid, true);
    }

    public Set<String> getIdentities(UUID uuid, boolean useCache) {
        if (useCache && cache.containsKey(uuid)) return new LinkedHashSet<>(cache.get(uuid));
        try (Connection connection = ds.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT identitys FROM identitys_players WHERE playerUUID=? order by priority");
            stmt.setString(1, uuid.toString());
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                cache.put(uuid, set.getString("identitys"));
            }
            stmt.close();
            set.close();
            return new LinkedHashSet<>(cache.get(uuid));
        } catch (SQLException e) {
            plugin.getPlatform().getLogger().severe("Error getting player identitys from MySQL - " + e);
            return null;
        }
    }

    @Override
    public void setIdentities(UUID uuid, Set<String> sets) {
        cache.removeAll(uuid);
        cache.putAll(uuid, sets);
        Task.builder()
            .action((Platform ignored) -> {
                try (Connection connection = ds.getConnection()) {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM identitys_players WHERE playerUuid=?");
                    stmt.setString(1, uuid.toString());
                    stmt.execute();
                    stmt.close();

                    PreparedStatement insStmt = connection.prepareStatement("INSERT INTO identitys_players VALUES (?,?,?)");

                    int i = 0;
                    for (String set : sets) {
                        insStmt.setString(1, uuid.toString());
                        insStmt.setInt(3, i);
                        insStmt.setString(2, set);

                        insStmt.addBatch();
                        i++;
                    }
                    insStmt.executeBatch();
                    insStmt.close();
                } catch (SQLException e) {
                    plugin.getPlatform().getLogger().severe("Error settings player identitys through MySQL - " + e);
                }
            })
            .async()
            .build().execute(plugin.getPlatform());
    }

    @Override
    public void clearIdentities(UUID uuid) {
        Task.builder()
            .action((Platform ignored) -> {
                try (Connection connection = ds.getConnection()) {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM identitys_players WHERE playerUuid=?");
                    stmt.setString(1, uuid.toString());
                    stmt.execute();
                    stmt.close();
                } catch (SQLException e) {
                    plugin.getPlatform().getLogger().severe("Error clearing player identitys through MySQL - " + e);
                }
            }).async().build().execute(plugin.getPlatform());
    }

    @Override
    public SetMultimap<UUID, String> getAllIdentities() {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM identitys_players ORDER BY priority");
            ResultSet set = stmt.executeQuery();
            SetMultimap<UUID, String> results = MultimapBuilder.hashKeys().hashSetValues().build();
            while (set.next()) {
                UUID uuid = UuidUtils.fromString(set.getString("playerUuid"));
                results.put(uuid, set.getString("identitys"));
            }
            stmt.close();
            set.close();
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getPlatform().getLogger().severe("Error getting identitys from MySQL - " + e);
        }
        return MultimapBuilder.hashKeys().hashSetValues().build();
    }
}