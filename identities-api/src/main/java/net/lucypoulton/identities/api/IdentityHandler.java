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

package net.lucypoulton.identities.api;

import net.kyori.adventure.text.Component;
import net.lucypoulton.identities.api.provider.IdentityProvider;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Manages users' identities. When using sets, take care to use a sorted implementation
 * such as {@link java.util.LinkedHashSet}.
 *
 * @since 2.0.0
 */
public interface IdentityHandler {
    /**
     * Sets the identities for a user.
     *
     * @param player the player whose identities to set
     * @param set the set of identities
     */
    void setUserIdentities(SquirtgunPlayer player, @NotNull Set<IdentitySet> set);
    /**
     * Gets a list of all current identity sets in use by players.
     *
     * @return a list of all current identity sets in use by players
     */
    Set<IdentitySet> getAllIdentities();

    /**
     *
     * @return the user's identities
     */
    @NotNull Set<IdentitySet> getIdentities(SquirtgunPlayer player);

    /**
    /**
     * Clears a user's identities.
     *
     * @param player the player to clear identities for
     */
    void clearUserIdentities(SquirtgunPlayer player);

    /**
     * Parses a string into a collection of identity sets. Appropriate for use in commands.
     *
     * @return the parsed sets
     */
    default ParseResult parse(String input) {
        return parse(input, true);
    }

    /**
     * Parses a string into a collection of identity sets. Appropriate for use in commands.
     *
     * @param enforceFilter whether to enforce the filter. Note that this field is ignored if the filter is disabled.
     * @return the parsed sets
     */
    ParseResult parse(String input, boolean enforceFilter);

    /**
     * Registers a {@link IdentityProvider} so its identity sets can be used.
     *
     * @param provider the provider to register
     * @since 1.4.0
     */
    void registerProvider(IdentityProvider provider);

    /**
     * The result of an attempt to parse a string to a set of identity sets.
     */
    class ParseResult {

        private final boolean success;
        private final Set<IdentitySet> results;
        private final List<Set<IdentitySet>> ambiguities;
        private final Component reason;

        public ParseResult(boolean success, Set<IdentitySet> results,
                           List<Set<IdentitySet>> ambiguities, Component reason) {
            this.success = success;
            this.results = results;
            this.ambiguities = ambiguities;
            this.reason = reason;
        }

        /**
         * Whether the set could (at least partially) successfully be parsed into sets.
         * When this is true, {@link #results()} will not be empty.
         */
        public boolean success() {
            return success;
        }

        /**
         * Inverse of {@link #success()}
         */
        public boolean failure() {
            return !success();
        }

        /**
         * The identity sets that the input was parsed into.
         */
        public @NotNull Set<IdentitySet> results() {
            return results;
        }

        /**
         * A list of sets derived from an ambiguous input.
         */
        public @NotNull List<Set<IdentitySet>> ambiguities() {
            return ambiguities;
        }

        /**
         * The reason for failure, if provided. When {@link #success()} is true, this will return null.
         */
        public @Nullable Component reason() {
            return reason;
        }
    }
}
