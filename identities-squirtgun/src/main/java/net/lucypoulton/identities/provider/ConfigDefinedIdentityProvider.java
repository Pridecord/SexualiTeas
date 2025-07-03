package net.lucypoulton.identities.provider;

import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.provider.IdentityProvider;
import net.lucypoulton.identities.api.set.IdentitySet;

import java.util.HashSet;
import java.util.Set;

public class ConfigDefinedIdentityProvider implements IdentityProvider {

    private final Identities plugin;

    private final Set<IdentitySet> sets = new HashSet<>();

    public ConfigDefinedIdentityProvider(Identities plugin) {
        this.plugin = plugin;
        reload();
    }

    private void reload() {
        sets.clear();

        for (String set : plugin.getConfigHandler().getPredefinedSets()) {
            IdentityHandler.ParseResult parsed = plugin.getIdentityHandler().parse(set);
            if (parsed.success()) {
                sets.addAll(parsed.results());
            } else {
                plugin.getPlatform().getLogger().warning("Predefined set '" + set + "' is invalid, ignoring");
            }
        }
    }

    @Override
    public Set<IdentitySet> get() {
        return null;
    }
}
