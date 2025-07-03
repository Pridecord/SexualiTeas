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

import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.kyori.adventure.text.Component;


public class ListIdentitiesNode extends AbstractNode<PermissionHolder> {
	private final IdentityHandler identityHandler;

	public ListIdentitiesNode(IdentityHandler identityHandler) {
		super("list", "Shows all predefined identity sets.", Condition.alwaysTrue());
		this.identityHandler = identityHandler;
	}

	@Override
	public Component execute(CommandContext context) {
		final FormatProvider fmt = context.getFormat();

		Component out = Component.empty()
				.append(fmt.formatTitle("All Predefined Identity Sets:"))
				.append(Component.newline());

		StringBuilder listBuilder = new StringBuilder();
		for (IdentitySet set : identityHandler.getAllIdentities()) {
			listBuilder.append(set.toString()).append("\n");
		}

		listBuilder.append("\n");

		out = out.append(Component.text(listBuilder.toString()))
				.append(fmt.formatFooter("*"));
		return out;
	}
}
