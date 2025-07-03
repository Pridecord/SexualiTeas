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

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import net.lucypoulton.identities.storage.Storage;
import net.lucypoulton.squirtgun.util.UuidUtils;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BungeeYamlFileStorage implements Storage {

    private final IdentitiesBungee pl;
    private File configFile;
    private Configuration config;

    private void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Failed to save data - " + e);
        }
    }

    public BungeeYamlFileStorage(IdentitiesBungee plugin) {
        this.pl = plugin;

        try {
            if (!pl.getDataFolder().exists()) pl.getDataFolder().mkdirs();
            configFile = new File(pl.getDataFolder(), "datastore.yml");

            if (configFile.createNewFile()) {
                Files.copy(plugin.getResourceAsStream("datastore.yml"), configFile.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Error while loading data store file - " + e);
        }
    }

    @Override
    public Set<String> getIdentities(UUID uuid) {
        List<String> list = config.getStringList("players." + uuid.toString());
		return new LinkedHashSet<>(list);
    }

    @Override
    public void setIdentities(UUID uuid, Set<String> sets) {
        config.set("players." + uuid.toString(), sets);
        save();
    }

    @Override
    public void clearIdentities(UUID uuid) {
        config.set("players." + uuid.toString(), new String[0]);
        save();
    }

	@Override
	public SetMultimap<UUID, String> getAllIdentities() {
        SetMultimap<UUID, String> out = MultimapBuilder.hashKeys().linkedHashSetValues().build();

    	// if this is null then something is seriously wrong
    	for (String uuid : Objects.requireNonNull(config.getSection("players")).getKeys()) {
    		out.putAll(UuidUtils.fromString(uuid), config.getStringList("players." + uuid));
		}
		return out;
	}
}
