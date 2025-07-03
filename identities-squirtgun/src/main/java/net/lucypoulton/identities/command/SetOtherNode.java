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

import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.identities.command.arguments.IdentitySetArgument;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.argument.OnlinePlayerArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetOtherNode extends AbstractNode<PermissionHolder> {
    private final Identities pl;
    private final IdentitySetArgument setsArg;
    private final CommandArgument<SquirtgunPlayer> playerArg;

    public SetOtherNode(Identities plugin) {
        super("setother", "Sets another player's identities.", Condition.hasPermission("identities.admin"));
        pl = plugin;
        playerArg = new OnlinePlayerArgument("player", "The player to set identities for",
                false, pl.getPlatform());
        setsArg = new IdentitySetArgument(pl.getIdentityHandler());
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(playerArg, setsArg);
    }

    @Override
    public Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();

        SquirtgunPlayer player = Objects.requireNonNull(context.getArgumentValue(playerArg));
        Set<IdentityHandler.ParseResult> setList = context.getArgumentValue(setsArg);

        Set<IdentitySet> newSets = new HashSet<>();
        for (IdentityHandler.ParseResult result : setList) {
            try {
                newSets.addAll(result.results());
            } catch (Exception e) {
                return fmt.getPrefix().append(fmt.formatMain("Invalid identity set"));
            }
        }

        pl.getIdentityHandler().setUserIdentities(player, newSets);

        return fmt.getPrefix()
                .append(fmt.formatMain("Set " + player.getUsername() + "'s identities to "))
                .append(fmt.formatAccent(IdentitySet.format(newSets)));
    }
}