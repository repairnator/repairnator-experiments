
package mage.cards.b;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.TapTargetCost;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.target.common.TargetControlledCreaturePermanent;

/**
 *
 * @author North
 */
public final class Bramblesnap extends CardImpl {

    public Bramblesnap(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{1}{G}");
        this.subtype.add(SubType.ELEMENTAL);

        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        this.addAbility(TrampleAbility.getInstance());
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD,
                new BoostSourceEffect(1, 1, Duration.EndOfTurn),
                new TapTargetCost(new TargetControlledCreaturePermanent())));
    }

    public Bramblesnap(final Bramblesnap card) {
        super(card);
    }

    @Override
    public Bramblesnap copy() {
        return new Bramblesnap(this);
    }
}
