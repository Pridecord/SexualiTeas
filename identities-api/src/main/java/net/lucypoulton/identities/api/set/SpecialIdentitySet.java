package net.lucypoulton.identities.api.set;

public class SpecialIdentitySet extends IdentitySet {

    private final IdentitySet parent;

    private final String formatted;
    private final String toStringValue;

    public SpecialIdentitySet(IdentitySet parent, String formatted, String toStringValue) {
        this.parent = parent;
        this.formatted = formatted;
        this.toStringValue = toStringValue;
    }

    @Override
    public String formatted() {
        return formatted;
    }

    @Override
    public String toString() {
        return toStringValue;
    }

    @Override
    public String nameForConcatenation() {
        return formatted;
    }

    @Override
    public String subjective() {
        return parent.subjective();
    }

    @Override
    public String objective() {
        return parent.objective();
    }

    @Override
    public String possessiveAdjective() {
        return parent.possessiveAdjective();
    }

    @Override
    public String possessiveIdentity() {
        return parent.possessiveIdentity();
    }

    @Override
    public String reflexive() {
        return parent.reflexive();
    }
}
