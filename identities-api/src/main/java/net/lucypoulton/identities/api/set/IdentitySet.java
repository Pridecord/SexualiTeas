package net.lucypoulton.identities.api.set;

import net.lucypoulton.identities.api.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class IdentitySet {
    public abstract String subjective();

    public abstract String objective();

    public abstract String possessiveAdjective();

    public abstract String possessiveIdentity();

    public abstract String reflexive();

    public String nameForConcatenation() {
        return subjective();
    }

    public String formatted() {
        return StringUtils.capitalise(subjective()) + "/" + StringUtils.capitalise(objective());
    }

    @Override
    public String toString() {
        return subjective() + "/" +
            objective() + "/" +
            possessiveAdjective() + "/" +
            possessiveIdentity() + "/" +
            reflexive();
    }

    public String[] asArray() {
        return new String[]{subjective(), objective(), possessiveAdjective(), possessiveIdentity(), reflexive()};
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IdentitySet && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) asArray());
    }

    public static IdentitySet parse(String input) {
        List<String> split = StringUtils.splitSet(input);
        if (split.size() != 5) {
            throw new IllegalArgumentException("Invalid number of identities in set");
        }
        return new ParsedIdentitySet(split.get(0), split.get(1), split.get(2), split.get(3), split.get(4));
    }

    public static String format(Collection<IdentitySet> sets) {
        if (sets.size() == 1) {
            return sets.stream().findFirst().orElseThrow().formatted();
        }
        return sets.stream().map(set -> StringUtils.capitalise(set.nameForConcatenation())).collect(Collectors.joining("/"));
    }
}
