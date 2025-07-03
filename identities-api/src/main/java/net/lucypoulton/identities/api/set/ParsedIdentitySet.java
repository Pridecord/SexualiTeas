package net.lucypoulton.identities.api.set;

public class ParsedIdentitySet extends IdentitySet {

    private final String subjective, objective, possessiveAdjective, possessiveIdentity, reflexive;

    public ParsedIdentitySet(String subjective, String objective,
                            String possessiveAdjective, String possessiveIdentity, String reflexive) {
        this.subjective = subjective;
        this.objective = objective;
        this.possessiveAdjective = possessiveAdjective;
        this.possessiveIdentity = possessiveIdentity;
        this.reflexive = reflexive;
    }

    @Override
    public String subjective() {
        return subjective;
    }

    @Override
    public String objective() {
        return objective;
    }


    @Override
    public String possessiveAdjective() {
        return possessiveAdjective;
    }

    @Override
    public String possessiveIdentity() {
        return possessiveIdentity;
    }

    @Override
    public String reflexive() {
        return reflexive;
    }
}
