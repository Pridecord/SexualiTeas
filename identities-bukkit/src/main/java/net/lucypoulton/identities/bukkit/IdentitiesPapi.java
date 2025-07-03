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

package net.lucypoulton.identities.bukkit;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.api.set.IdentitySet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class IdentitiesPapi extends PlaceholderExpansion {

    private IdentitySet unsetIdentitySet;

    private final Identities plugin;

    public IdentitiesPapi(Identities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getAuthors());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "identities";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginVersion().toString();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (unsetIdentitySet == null) {
            unsetIdentitySet = plugin.getIdentityHandler().parse("unset").results().stream().findFirst().orElseThrow();
        }

        if (player == null) return "";

        Set<IdentitySet> allSets = plugin.getIdentityHandler().getIdentities(plugin.getPlatform().getPlayer(player.getUniqueId()));
        IdentitySet mainIdentities = allSets.size() > 0 ? allSets.iterator().next() : unsetIdentitySet;

        String[] split = identifier.split("_");
        String ident = split[0];

        String feedback;

        switch (ident) {
            case "identities":
                feedback = IdentitySet.format(allSets.size() == 0 ? Collections.singleton(unsetIdentitySet) : allSets);
                break;
            case "all":
                feedback = mainIdentities.toString();
                break;
            case "objective":
                feedback = mainIdentities.objective();
                break;
            case "subjective":
                feedback = mainIdentities.subjective();
                break;
            case "possessiveadj":
                feedback = mainIdentities.possessiveAdjective();
                break;
            case "possessivepro":
                feedback = mainIdentities.possessiveIdentity();
                break;
            case "reflexive":
                feedback = mainIdentities.reflexive();
                break;
            default:
                return "";
        }

        for (int idx = 1; idx < split.length; idx++) {
            String mod = split[idx];
            switch (mod.toLowerCase(Locale.ROOT)) {
                case "upper":
                    feedback = feedback.toUpperCase();
                    break;
                case "lower":
                    feedback = feedback.toLowerCase();
                    break;
                case "capital":
                    feedback = feedback.substring(0, 1).toUpperCase() + feedback.substring(1).toLowerCase();
                    break;
                case "nounset":
                    if (feedback.equalsIgnoreCase("unset")) return "";
            }
        }
        return feedback;
    }
}
