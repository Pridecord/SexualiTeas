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

package net.lucypoulton.identities.bukkit;

import net.lucypoulton.identities.config.ConfigHandler;
import net.lucypoulton.identities.config.ConnectionType;
import net.lucypoulton.identities.config.SqlInfoContainer;
import net.lucypoulton.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BukkitConfigHandler implements ConfigHandler {
    private final IdentitiesBukkit pl;
    private final HashMap<TextDecoration, Character> decoStrings = new HashMap<>();

    public BukkitConfigHandler(IdentitiesBukkit plugin) {
        pl = plugin;
        FileConfiguration cfg = pl.getConfig();
        cfg.options().copyDefaults(true);
        cfg.options().header("Identities Config File\n" +
                "Make changes here and update them by either using /ide reload\n" +
                "or by restarting the server\n" +
                "NOTE: if you're using predefined sets with MySQL, make sure they match on\n" +
                "all servers!\n" +
                "Documentation at https://docs.lucypoulton.net/identities\n" +
                "Support discord at https://discord.lucypoulton.net");

        cfg.addDefault("checkForUpdates", "true");
        cfg.addDefault("accent", "{#fa9efa>}%s{#9dacfa<}");
        cfg.addDefault("main", "&f");

        cfg.addDefault("connection", "yml");
        cfg.addDefault("mysql.host", "127.0.0.1");
        cfg.addDefault("mysql.port", 3306);
        cfg.addDefault("mysql.database", "identities");
        cfg.addDefault("mysql.username", "identities");
        cfg.addDefault("mysql.password", "password");

        cfg.addDefault("predefinedSets", new ArrayList<String>());

        cfg.addDefault("filter.enabled", "true");
        cfg.addDefault("filter.patterns", new String[]{"apache+", "hel+icop+ter"});

        pl.saveConfig();

        decoStrings.put(TextDecoration.OBFUSCATED, 'k');
        decoStrings.put(TextDecoration.BOLD, 'l');
        decoStrings.put(TextDecoration.STRIKETHROUGH, 'm');
        decoStrings.put(TextDecoration.UNDERLINED, 'n');
        decoStrings.put(TextDecoration.ITALIC, 'o');
    }

    private String getString(String key) {
        return getString(key, null);
    }

    private String getString(String key, String defaultVal) {
        String value = pl.getConfig().getString(key);
        if (value == null) {
            if (defaultVal == null) {
                pl.getLogger().severe("Your config file is broken! Unable to read key '" + key + "'");
                return null;
            }
            return defaultVal;
        }
        return value;
    }

    private String serialiseFormatters(TextDecoration... formatters) {
        if (formatters == null) return null;
        StringBuilder out = new StringBuilder();
        for (TextDecoration deco : formatters) out.append(decoStrings.get(deco));
        return out.toString();
    }

    private Component applyFormatter(String formatter, String content, String formatters) {
        return formatter.contains("%s") ?
                TextFormatter.format(String.format(formatter, content), formatters, true) :
                TextFormatter.format(formatter + content, formatters, true);
    }

    public String getAccentColour() {
        return getString("accent", "&3");
    }

    @Override
    public Component formatAccent(@NotNull String s, TextDecoration... formatters) {
        return applyFormatter(getAccentColour(), s, serialiseFormatters(formatters));
    }

    public String getMainColour() {
        return getString("main", "&f");
    }

    @Override
    public Component formatMain(@NotNull String s, TextDecoration... formatters) {
        return applyFormatter(getMainColour(), s, serialiseFormatters(formatters));
    }

    @SuppressWarnings("ConstantConditions")
    public Component getPrefix() {
        String prefix = getString("format.prefix", "");
        if (prefix.equals("")) return formatAccent("Identities")
                .append(Component.text(" >> ").color(NamedTextColor.GRAY));
        return TextFormatter.format(getString("format.prefix"));
    }

    private boolean getBoolValue(String key) {
        return !"false".equals(getString(key, "true"));
    }

    public List<String> getPredefinedSets() {
        return pl.getConfig().getStringList("predefinedSets");
    }

    public List<String> getFilterPatterns() {
        return pl.getConfig().getStringList("filter.patterns");
    }

    public boolean filterEnabled() {
        return getBoolValue("filter.enabled");
    }

    public ConnectionType getConnectionType() {
        return ConnectionType.valueOf(getString("connection").toUpperCase());
    }

    public SqlInfoContainer getSqlConnectionData() {
        return new SqlInfoContainer(
                getString("mysql.host"),
                pl.getConfig().getInt("mysql.port", 3306),
                getString("mysql.database"),
                getString("mysql.username"),
                getString("mysql.password")
        );
    }

    public boolean checkForUpdates() {
        return getBoolValue("checkForUpdates");
    }
}
