

package mage.cards.m;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.common.continuous.GainAbilityControlledEffect;
import mage.abilities.keyword.HasteAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;

/**
 *
 * @author Loki
 */
public final class MadrushCyclops extends CardImpl {

    public MadrushCyclops (UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{1}{B}{R}{G}");
        this.subtype.add(SubType.CYCLOPS);
        this.subtype.add(SubType.WARRIOR);
        


        this.power = new MageInt(3);
        this.toughness = new MageInt(4);
        
        // Creatures you control have haste.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new GainAbilityControlledEffect(HasteAbility.getInstance(), Duration.WhileOnBattlefield, new FilterCreaturePermanent())));
    }

    public MadrushCyclops (final MadrushCyclops card) {
        super(card);
    }

    @Override
    public MadrushCyclops copy() {
        return new MadrushCyclops(this);
    }

}
