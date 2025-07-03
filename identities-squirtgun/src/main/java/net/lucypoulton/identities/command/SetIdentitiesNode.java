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
import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.identities.command.arguments.IdentitySetArgument;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import net.lucypoulton.squirtgun.command.node.AbstractNode;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SetIdentitiesNode extends AbstractNode<SquirtgunPlayer> {
    private final Identities pl;
    private final IdentitySetArgument sets;

    public SetIdentitiesNode(Identities plugin) {
        super("set", "Sets your identities.", Condition.isPlayer());
        pl = plugin;
        sets = new IdentitySetArgument(pl.getIdentityHandler());
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return ImmutableList.of(sets);
    }

    static Pair<Set<IdentitySet>, Component> parse(Set<IdentityHandler.ParseResult> parseResult, FormatProvider format) {
        Set<IdentitySet> newSets = new LinkedHashSet<>();
        Component errorMessages = Component.empty();

        // required argument - this is safe
        for (IdentityHandler.ParseResult results : Objects.requireNonNull(parseResult)) {
            if (results.success()) {
                newSets.addAll(results.results());
            } else {
                Component reason = results.reason();
                return new Pair<>(Set.of(),
                    format.getPrefix().append(reason != null ? reason : format.formatMain("There was an error."))
                );
            }
            if (!results.ambiguities().isEmpty()) {
                for (Set<IdentitySet> ambiguity : results.ambiguities()) {
                    errorMessages = errorMessages.append(
                        format.formatMain("Ambiguous set detected, assuming you meant ")
                            .append(format.formatAccent(ambiguity.stream().findFirst().orElseThrow().toString()))
                            .append(format.formatMain("."))
                            .append(Component.newline())
                    );
                }
            }
        }
        return new Pair<>(newSets, errorMessages);
    }

    @Override
    public Component execute(CommandContext context) {
        final FormatProvider fmt = context.getFormat();

        SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();
        Set<IdentityHandler.ParseResult> setList = context.getArgumentValue(sets);

        Pair<Set<IdentitySet>, Component> result = parse(setList, fmt);
        Set<IdentitySet> newSets = result.key();
        Component errorMessages = result.value();

        if (!newSets.isEmpty()) {
            pl.getIdentityHandler().setUserIdentities(player, newSets);
            errorMessages = errorMessages.append(fmt.getPrefix()
                .append(fmt.formatMain("Set identities to "))
                .append(fmt.formatAccent(IdentitySet.format(newSets)))
            );
        }
        return errorMessages;
    }
}