
package mage.cards.v;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.TransformSourceEffect;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.abilities.keyword.TransformAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.d.DronepackKindred;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.Zone;

/**
 *
 * @author fireshoes
 */
public final class VildinPackOutcast extends CardImpl {

    public VildinPackOutcast(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{4}{R}");
        this.subtype.add(SubType.WEREWOLF);
        this.subtype.add(SubType.HORROR);
        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        this.transformable = true;
        this.secondSideCardClazz = DronepackKindred.class;

        // Trample
        this.addAbility(TrampleAbility.getInstance());

        // {R}: Vildin-Pack Outcast gets +1/-1 until end of turn.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new BoostSourceEffect(1, -1, Duration.EndOfTurn), new ManaCostsImpl("{R}")));

        // {5}{R}{R}: Transform Vildin-Pack Outcast.
        this.addAbility(new TransformAbility());
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new TransformSourceEffect(true), new ManaCostsImpl("{5}{R}{R}")));
    }

    public VildinPackOutcast(final VildinPackOutcast card) {
        super(card);
    }

    @Override
    public VildinPackOutcast copy() {
        return new VildinPackOutcast(this);
    }
}
