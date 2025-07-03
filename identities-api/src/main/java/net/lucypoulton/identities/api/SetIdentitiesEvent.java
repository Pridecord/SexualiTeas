package net.lucypoulton.identities.api;

import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.player.AbstractCancellablePlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SetIdentitiesEvent extends AbstractCancellablePlayerEvent {
    private final SquirtgunPlayer player;
    private final Set<IdentitySet> set;

    public SetIdentitiesEvent(SquirtgunPlayer player, Set<IdentitySet> set) {
        super(player);
        this.player = player;
        this.set = set;
    }

    @Override
    public @NotNull SquirtgunPlayer player() {
        return player;
    }

    public Set<IdentitySet> getSet() {
        return set;
    }
}
