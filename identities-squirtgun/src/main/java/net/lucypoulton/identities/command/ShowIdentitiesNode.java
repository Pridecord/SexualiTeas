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

package net.lucypoulton.identities.command;

import com.google.common.collect.ImmutableList;
import net.kyori.adventure.text.Component;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.argument.OnlinePlayerArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ShowIdentitiesNode extends AbstractNode<PermissionHolder> {
    private final IdentityHandler handler;
    private final CommandArgument<SquirtgunPlayer> playerArgument;

    public ShowIdentitiesNode(Platform platform, IdentityHandler handler) {
        super("show", "Shows your, or another player's, identities.", Condition.alwaysTrue());
        this.handler = handler;
        playerArgument = new OnlinePlayerArgument("player", "the player to get identities for",
            true, platform);
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return ImmutableList.of(playerArgument);
    }

    @Override
    public Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();

        SquirtgunPlayer commandTarget = context.getArgumentValue(playerArgument);

        if (commandTarget == null) {
            if (!(context.getTarget() instanceof SquirtgunPlayer)) {
                return fmt.getPrefix().append(fmt.formatMain("That player could not be found."));
            }
            commandTarget = (SquirtgunPlayer) context.getTarget();
        }

        Set<IdentitySet> sets = handler.getIdentities(commandTarget);

        return fmt.getPrefix()
            .append(fmt.formatMain(commandTarget.getUsername() + "'s identitys are "))
            .append(fmt.formatAccent(
                sets.isEmpty() ? "unset" : IdentitySet.format(sets)
            ));
    }
}
