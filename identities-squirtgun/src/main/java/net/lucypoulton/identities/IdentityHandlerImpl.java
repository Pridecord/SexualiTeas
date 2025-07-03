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

package net.lucypoulton.identities;

import net.kyori.adventure.text.event.ClickEvent;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.SetIdentitiesEvent;
import net.lucypoulton.identities.api.StringUtils;
import net.lucypoulton.identities.api.provider.IdentityProvider;
import net.lucypoulton.identities.api.set.ParsedIdentitySet;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.identities.api.set.SpecialIdentitySet;
import net.lucypoulton.identities.storage.Storage;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunPlayer;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.PluginReloadEvent;
import net.lucypoulton.squirtgun.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class IdentityHandlerImpl implements IdentityHandler {
    private final Storage storage;
    private final List<Pattern> filterPatterns = new ArrayList<>();

    public Storage getStorage() {
        return storage;
    }

    private final Identities pl;
    private final Set<IdentityProvider> providers = Collections.newSetFromMap(new IdentityHashMap<>());

    public IdentityHandlerImpl(Identities pl, Storage storage) {
        this.pl = pl;
        this.storage = storage;
        pl.getPlatform().getEventManager().register(EventHandler.executes(PluginReloadEvent.class, e -> reloadFilterPatterns()));
        reloadFilterPatterns();
    }

    private void reloadFilterPatterns() {
        filterPatterns.clear();
        for (String pattern : pl.getConfigHandler().getFilterPatterns()) {
            filterPatterns.add(Pattern.compile(pattern));
        }
    }

    @Override
    public void setUserIdentities(SquirtgunPlayer player, @NotNull Set<IdentitySet> set) {
        if (pl.getPlatform().getEventManager().dispatch(new SetIdentitiesEvent(player, set)).successful()) {
            storage.setIdentities(player.getUuid(),
                    set.stream().map(Object::toString).collect(Collectors.toCollection(LinkedHashSet::new)));
        }
    }

    @Override
    public Set<IdentitySet> getAllIdentities() {
        return providers.stream().flatMap(x -> x.get().stream()).collect(toSet());
    }

    @Override
    public @NotNull Set<IdentitySet> getIdentities(SquirtgunPlayer player) {
        return storage.getIdentities(player.getUuid()).stream()
                .map(this::parse)
                .flatMap(result -> result.results().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void clearUserIdentities(SquirtgunPlayer player) {
        storage.clearIdentities(player.getUuid());
    }

    private int equalityScore(List<String> input, IdentitySet two) {
        if (two instanceof SpecialIdentitySet && input.get(0).equalsIgnoreCase(two.nameForConcatenation())) {
            return 7;
        }
        int i = 0;
        String[] setArray = two.asArray();
        while (i < input.size() && i < 6 && input.get(i).equalsIgnoreCase(setArray[i])) {
            i++;
        }
        return i;
    }

    @Override
    public ParseResult parse(String input, boolean enforceFilter) {
        final List<String> split = StringUtils.splitSet(input);

        if (pl.getConfigHandler().filterEnabled() && enforceFilter
            && filterPatterns.stream().anyMatch(pattern -> pattern.matcher(input).find())) {
            return new ParseResult(false, Set.of(), List.of(),
                pl.getConfigHandler().formatMain("You can't use that set."));
        }

        final Set<IdentitySet> out = new LinkedHashSet<>();

        int i = 0;

        while (split.size() - i >= 5) {
            out.add(new ParsedIdentitySet(split.get(i),
                split.get(i + 1),
                split.get(i + 2),
                split.get(i + 3),
                split.get(i + 4)
            ));
            i += 5;
        }

        final List<Set<IdentitySet>> ambiguities = new ArrayList<>();
        while (i < split.size()) {
            String identity = split.get(i);
            int finalI = i;
            List<Pair<IdentitySet, Integer>> sets = providers.stream()
                .flatMap(provider -> provider.get().stream())
                .filter(set -> set.nameForConcatenation().equalsIgnoreCase(identity))
                .map(set -> new Pair<>(set, equalityScore(split.subList(finalI, split.size()), set)))
                .collect(toList());

            int max = sets.stream().mapToInt(Pair::value).max().orElse(0);
            if (max == 0) {
                i++;
                continue;
            }
            Set<IdentitySet> potentialSets = sets.stream().filter(x -> x.value() == max)
                .map(Pair::key)
                .collect(Collectors.toSet());
            i += max;
            if (potentialSets.size() != 1) {
                ambiguities.add(potentialSets);
            }
            out.add(potentialSets.stream().findFirst().orElseThrow());
        }

        if (out.isEmpty()) {
            final FormatProvider fmt = pl.getConfigHandler();
            return new ParseResult(false, out, ambiguities,
                fmt.formatMain("Unrecognised identity ")
                    .append(fmt.formatAccent(input))
                    .append(fmt.formatMain(". Click here for help.")
                        .clickEvent(ClickEvent.openUrl("https://docs.lucypoulton.net/identities/formats/#setting-your-identities"))
                    )
            );
        }
        return new ParseResult(true, out, ambiguities, null);
    }

    @Override
    public void registerProvider(IdentityProvider provider) {
        this.providers.add(provider);
    }
}