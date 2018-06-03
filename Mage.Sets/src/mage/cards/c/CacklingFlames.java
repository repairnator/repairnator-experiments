
package mage.cards.c;

import java.util.UUID;
import mage.abilities.condition.InvertCondition;
import mage.abilities.condition.common.HellbentCondition;
import mage.abilities.decorator.ConditionalOneShotEffect;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.target.common.TargetAnyTarget;

/**
 *
 * @author JotaPeRL
 */
public final class CacklingFlames extends CardImpl {

    public CacklingFlames(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.INSTANT}, "{3}{R}");

        // Cackling Flames deals 3 damage to any target.
        this.getSpellAbility().addEffect(new ConditionalOneShotEffect(
                new DamageTargetEffect(3),
                new InvertCondition(HellbentCondition.instance),
                "{this} deals 3 damage to any target"));
        // Hellbent - Cackling Flames deals 5 damage to that creature or player instead if you have no cards in hand.
        this.getSpellAbility().addEffect(new ConditionalOneShotEffect(
                new DamageTargetEffect(5),
                HellbentCondition.instance,
                "<br/><br/><i>Hellbent</i> &mdash; {this} deals 5 damage to that permanent or player instead if you have no cards in hand."));

        this.getSpellAbility().addTarget(new TargetAnyTarget());
    }

    public CacklingFlames(final CacklingFlames card) {
        super(card);
    }

    @Override
    public CacklingFlames copy() {
        return new CacklingFlames(this);
    }
}
