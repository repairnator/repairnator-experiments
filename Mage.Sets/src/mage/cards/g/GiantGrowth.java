

package mage.cards.g;

import java.util.UUID;
import mage.abilities.effects.common.continuous.BoostTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public final class GiantGrowth extends CardImpl {

    public GiantGrowth(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{G}");

        this.getSpellAbility().addTarget(new TargetCreaturePermanent());
        this.getSpellAbility().addEffect(new BoostTargetEffect(3, 3, Duration.EndOfTurn));
    }

    public GiantGrowth(final GiantGrowth card) {
        super(card);
    }

    @Override
    public GiantGrowth copy() {
        return new GiantGrowth(this);
    }
}
