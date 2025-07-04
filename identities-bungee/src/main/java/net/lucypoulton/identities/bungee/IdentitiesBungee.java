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

package net.lucypoulton.identities.bungee;

import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.IdentitiesPlatform;
import net.lucypoulton.identities.storage.MysqlConnectionException;
import net.lucypoulton.identities.storage.MysqlFileStorage;
import net.lucypoulton.identities.storage.Storage;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimplePie;

import java.io.IOException;

public final class IdentitiesBungee extends Plugin {

    private Identities plugin;
    private BungeeConfigHandler configHandler;
    private Storage storage;

    public Identities getPlugin() {
        return plugin;
    }

    public BungeeConfigHandler getConfigHandler() {
        return configHandler;
    }

    public Storage getStorage() {
        return storage;
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 11719);

        try {
            configHandler = new BungeeConfigHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        metrics.addCustomChart(new SimplePie("storage_backend", () -> configHandler.getConnectionType().name()));

        IdentitiesPlatform platform = new IdentitiesBungeePlatform(this);
        plugin = new Identities(platform);

        switch (configHandler.getConnectionType()) {
            case YML:
                storage = new BungeeYamlFileStorage(this);
                break;
            case MYSQL:
                try {
                    storage = new MysqlFileStorage(plugin);
                    break;
                } catch (MysqlConnectionException e) {
                    return;
                }
        }

        plugin.onEnable();
    }
}