package net.lucypoulton.identities.bukkit;

import net.lucypoulton.identities.Identities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PapiRegisterListener implements Listener {
    private final Identities platform;

    public PapiRegisterListener(Identities platform) {
        this.platform = platform;
    }

    @EventHandler
    public void onRegister(PluginEnableEvent e) {
        if (!e.getPlugin().getName().equals("PlaceholderAPI")) return;
        new IdentitiesPapi(platform).register();
    }
}
