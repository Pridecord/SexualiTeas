package net.lucypoulton.identities.bukkit;

import net.lucypoulton.identities.IdentitiesPlatform;
import net.lucypoulton.identities.config.ConfigHandler;
import net.lucypoulton.identities.storage.Storage;
import net.lucypoulton.squirtgun.bukkit.BukkitPlatform;

public class IdentitiesBukkitPlatform extends BukkitPlatform implements IdentitiesPlatform {
    private final IdentitiesBukkit plugin;
    public IdentitiesBukkitPlatform(IdentitiesBukkit plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public ConfigHandler getConfigHandler() {
        return plugin.getConfigHandler();
    }

    @Override
    public Storage getStorage() {
        return plugin.getStorage();
    }

    @Override
    public void reloadConfig() {
        plugin.reloadConfig();
    }

    @Override
    public String name() {
        return "Bukkit (Identities)";
    }
}
