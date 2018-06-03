
package mage.cards.a;

import java.util.UUID;
import mage.MageInt;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;

/**
 *
 * @author fireshoes
 */
public final class AncientCarp extends CardImpl {

    public AncientCarp(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{4}{U}");
        this.subtype.add(SubType.FISH);
        this.power = new MageInt(2);
        this.toughness = new MageInt(5);
    }

    public AncientCarp(final AncientCarp card) {
        super(card);
    }

    @Override
    public AncientCarp copy() {
        return new AncientCarp(this);
    }
}
