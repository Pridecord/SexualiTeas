package net.lucypoulton.identities.bungee;

import net.lucypoulton.identities.IdentitiesPlatform;
import net.lucypoulton.identities.storage.Storage;
import net.lucypoulton.squirtgun.bungee.BungeePlatform;

import java.io.IOException;

public class IdentitiesBungeePlatform extends BungeePlatform implements IdentitiesPlatform {
    private final IdentitiesBungee plugin;
    public IdentitiesBungeePlatform(IdentitiesBungee plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public BungeeConfigHandler getConfigHandler() {
        return plugin.getConfigHandler();
    }

    @Override
    public Storage getStorage() {
        return plugin.getStorage();
    }

    @Override
    public void reloadConfig() {
        try {
            plugin.getConfigHandler().reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return "BungeeCord (Identities)";
    }
}
