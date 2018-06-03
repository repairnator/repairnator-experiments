
package mage.cards.r;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.BlocksTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;

/**
 *
 * @author fireshoes
 */
public final class RoyalTrooper extends CardImpl {

    public RoyalTrooper(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{2}{W}");
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.SOLDIER);
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // Whenever Royal Trooper blocks, it gets +2/+2 until end of turn.
        Effect effect = new BoostSourceEffect(2, 2, Duration.EndOfTurn);
        effect.setText("it gets +2/+2 until end of turn");
        this.addAbility(new BlocksTriggeredAbility(effect, false));
    }

    public RoyalTrooper(final RoyalTrooper card) {
        super(card);
    }

    @Override
    public RoyalTrooper copy() {
        return new RoyalTrooper(this);
    }
}
