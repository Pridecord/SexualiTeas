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
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PreviewNode extends AbstractNode<PermissionHolder> {
	private final Identities pl;

	public PreviewNode(Identities plugin) {
		super("preview", "Test out your identity selection", Condition.alwaysTrue());
		pl = plugin;
	}

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return Collections.emptyList();
	}

	private String capitalise(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

	@Override
	public Component execute(final CommandContext context) {
		final FormatProvider fmt = context.getFormat();
		if (!(context.getTarget() instanceof SquirtgunPlayer)) {
			return fmt.getPrefix()
					.append(fmt.formatMain("Only players can use this command."));
		}

		final SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();

		final Set<IdentitySet> sets = pl.getIdentityHandler().getIdentities(player);
		if (sets.size() == 0) {
			return fmt.getPrefix()
					.append(fmt.formatMain("You haven't set any identities yet!"));
		}

		final IdentitySet set = sets.iterator().next();

		// yes, this is messy, but java compiles it to a stringbuilder so its all good
		String builder = player.getUsername() + " is testing " + set.possessiveAdjective() +
				" identity selection.\n" +
				"Have you seen " + player.getUsername() + "? " +
				capitalise(set.subjective()) + " asked me to help with " +
				set.objective() + " build.\n" +
				player.getUsername() + " has been spending all " + set.possessiveAdjective() +
				" time on this server.";
		return Component.text(builder);
	}
}
