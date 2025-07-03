package net.lucypoulton.identities.api.provider;

import net.lucypoulton.identities.api.set.IdentitySet;

import java.util.Set;

@FunctionalInterface
public interface IdentityProvider {
    Set<IdentitySet> get();
}
