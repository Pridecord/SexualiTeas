package net.lucypoulton.identities.provider;

import net.lucypoulton.identities.api.provider.IdentityProvider;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.identities.api.set.SpecialIdentitySet;

import java.util.Set;

public class BuiltinIdentityProvider implements IdentityProvider {

    private final IdentitySet baseSet = IdentitySet.parse("other/other/other/other/other");
    private final Set<IdentitySet> sets = Set.of(
            baseSet,
            new SpecialIdentitySet(baseSet, "Agender", "Agender"),
            new SpecialIdentitySet(baseSet, "Aromantic", "Aromantic"),
            new SpecialIdentitySet(baseSet, "Asexual", "Asexual"),
            new SpecialIdentitySet(baseSet, "Bigender", "Bigender"),
            new SpecialIdentitySet(baseSet, "Bisexual", "Bisexual"),
            new SpecialIdentitySet(baseSet, "Demiboy", "Demiboy"),
            new SpecialIdentitySet(baseSet, "Demigender", "Demigender"),
            new SpecialIdentitySet(baseSet, "Demigirl", "Demigirl"),
            new SpecialIdentitySet(baseSet, "Demisexual", "Demisexual"),
            new SpecialIdentitySet(baseSet, "Gay", "Gay"),
            new SpecialIdentitySet(baseSet, "Genderfluid", "Genderfluid"),
            new SpecialIdentitySet(baseSet, "Genderqueer", "Genderqueer"),
            new SpecialIdentitySet(baseSet, "Graysexual", "Graysexual"),
            new SpecialIdentitySet(baseSet, "Intersex", "Intersex"),
            new SpecialIdentitySet(baseSet, "Lesbian", "Lesbian"),
            new SpecialIdentitySet(baseSet, "Neutrois", "Neutrois"),
            new SpecialIdentitySet(baseSet, "Nonbinary", "Nonbinary"),
            new SpecialIdentitySet(baseSet, "Omnisexual", "Omnisexual"),
            new SpecialIdentitySet(baseSet, "Pangender", "Pangender"),
            new SpecialIdentitySet(baseSet, "Pansexual", "Pansexual"),
            new SpecialIdentitySet(baseSet, "Polyamorous", "Polyamorous"),
            new SpecialIdentitySet(baseSet, "Polygender", "Polygender"),
            new SpecialIdentitySet(baseSet, "Queer", "Queer"),
            new SpecialIdentitySet(baseSet, "Questioning", "Questioning"),
            new SpecialIdentitySet(baseSet, "Skoliosexual", "Skoliosexual"),
            new SpecialIdentitySet(baseSet, "Transfeminine", "Transfeminine"),
            new SpecialIdentitySet(baseSet, "Transgender", "Transgender"),
            new SpecialIdentitySet(baseSet, "Transmasculine", "Transmasculine"),
            new SpecialIdentitySet(baseSet, "Two-Spirit", "Two-Spirit"),
            new SpecialIdentitySet(baseSet, "Androgyne", "Androgyne"),
            new SpecialIdentitySet(baseSet, "Maverique", "Maverique"),
            new SpecialIdentitySet(baseSet, "Abrosexual", "Abrosexual"),
            new SpecialIdentitySet(baseSet, "Aegosexual", "Aegosexual"),
            new SpecialIdentitySet(baseSet, "Aporagender", "Aporagender"),
            new SpecialIdentitySet(baseSet, "Ceterosexual", "Ceterosexual"),
            new SpecialIdentitySet(baseSet, "Cupiosexual", "Cupiosexual"),
            new SpecialIdentitySet(baseSet, "Lithromantic", "Lithromantic"),
            new SpecialIdentitySet(baseSet, "Reciprosexual", "Reciprosexual"),
            new SpecialIdentitySet(baseSet, "Sapiosexual", "Sapiosexual"),
            new SpecialIdentitySet(baseSet, "Aegoromantic", "Aegoromantic"),
            new SpecialIdentitySet(baseSet, "Akiosexual", "Akiosexual"),
            new SpecialIdentitySet(baseSet, "Aliagender", "Aliagender"),
            new SpecialIdentitySet(baseSet, "Ambonec", "Ambonec"),
            new SpecialIdentitySet(baseSet, "Autigender", "Autigender"),
            new SpecialIdentitySet(baseSet, "Cassgender", "Cassgender"),
            new SpecialIdentitySet(baseSet, "Cloudgender", "Cloudgender"),
            new SpecialIdentitySet(baseSet, "Demiflux", "Demiflux"),
            new SpecialIdentitySet(baseSet, "Femme", "Femme"),
            new SpecialIdentitySet(baseSet, "Grayromantic", "Grayromantic"),
            new SpecialIdentitySet(baseSet, "Intergender", "Intergender"),
            new SpecialIdentitySet(baseSet, "Novigender", "Novigender"),
            new SpecialIdentitySet(baseSet, "Quoigender", "Quoigender"),
            new SpecialIdentitySet(baseSet, "Trigender", "Trigender"),
            new SpecialIdentitySet(baseSet, "Venngender", "Venngender"),
            new SpecialIdentitySet(baseSet, "Xenogender", "Xenogender"),
            new SpecialIdentitySet(baseSet, "Unset", "Unset")
    );

    @Override
    public Set<IdentitySet> get() {
        return sets;
    }
}
