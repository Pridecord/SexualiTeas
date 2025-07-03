package net.lucypoulton.identities;

import net.lucypoulton.identities.config.ConfigHandler;
import net.lucypoulton.identities.storage.Storage;
import net.lucypoulton.squirtgun.platform.Platform;

public interface IdentitiesPlatform extends Platform {
    ConfigHandler getConfigHandler();

    Storage getStorage();

    void reloadConfig();
}
