package net.lucypoulton.pronouns.provider;

import net.lucypoulton.pronouns.api.provider.PronounProvider;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.SpecialPronounSet;

import java.util.Set;

public class BuiltinPronounProvider implements PronounProvider {

    private final PronounSet baseSet = PronounSet.parse("they/them/their/theirs/themself");
    private final Set<PronounSet> sets = Set.of(
        baseSet,
        PronounSet.parse("he/him/his/his/himself"),
        PronounSet.parse("she/her/her/hers/herself"),
        PronounSet.parse("ae/aer/aer/aers/aerself"),
        PronounSet.parse("ae/aem/aer/aers/aemself"),
        PronounSet.parse("ay/aym/ays/ays/aymself"),
        PronounSet.parse("ce/cir/cir/cirs/cirself"),
        PronounSet.parse("co/cos/cos/cos/coself"),
        PronounSet.parse("e/em/eir/eirs/eirself"),
        PronounSet.parse("ey/em/eir/eirs/eirself"),
        PronounSet.parse("hir/hir/hir/hirs/hirself"),
        PronounSet.parse("hu/hu/hume/hume/humeself"),
        PronounSet.parse("jee/jem/jeir/jeirs/jemself"),
        PronounSet.parse("kye/kyr/kyne/kynes/kyrself"),
        PronounSet.parse("lee/lim/lis/lis/limself"),
        PronounSet.parse("me/mim/mis/mis/mimself"),
        PronounSet.parse("me/mem/meir/meirs/memself"),
        PronounSet.parse("me/mem/meir/meirs/meirself"),
        PronounSet.parse("me/me/mes/mes/meself"),
        PronounSet.parse("ne/nem/neir/neirs/neirself"),
        PronounSet.parse("ne/nem/nir/nirs/nemself"),
        PronounSet.parse("ne/nir/nir/nirs/nemself"),
        PronounSet.parse("ne/nym/nis/nis/nymself"),
        PronounSet.parse("per/per/pers/pers/perself"),
        PronounSet.parse("sie/sier/sier/siers/sierself"),
        PronounSet.parse("te/tim/tis/tis/timself"),
        PronounSet.parse("thon/thon/thons/thons/thonself"),
        PronounSet.parse("xe/hir/hir/hirs/hirself"),
        PronounSet.parse("xe/xim/xis/xis/ximself"),
        PronounSet.parse("xe/xir/xir/xirs/xirself"),
        PronounSet.parse("xe/xem/xyr/xyrs/xemself"),
        PronounSet.parse("xie/xem/xyr/xyrs/xemself"),
        PronounSet.parse("ze/hir/hir/hirs/hirself"),
        PronounSet.parse("ze/zir/zir/zirs/zirself"),
        PronounSet.parse("zed/zed/zed/zeds/zedself"),
        PronounSet.parse("ze/zan/zan/zans/zanself"),
        PronounSet.parse("zed/zed/zeir/zeirs/zeirself"),
        PronounSet.parse("zhe/zhim/zhir/zhirs/zhirself"),
        PronounSet.parse("zhe/zhir/zhir/zhirs/zhirself"),
        PronounSet.parse("zie/zir/zir/zirs/zirself"),
        new SpecialPronounSet(baseSet, "Any", "Any"),
        new SpecialPronounSet(baseSet, "Ask", "Ask"),
        new SpecialPronounSet(baseSet, "Other", "Other"),
        new SpecialPronounSet(baseSet, "Unset", "Unset")
    );

    @Override
    public Set<PronounSet> get() {
        return sets;
    }
}
