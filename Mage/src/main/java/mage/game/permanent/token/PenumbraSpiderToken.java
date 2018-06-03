
package mage.game.permanent.token;

import mage.constants.CardType;
import mage.constants.SubType;
import mage.MageInt;
import mage.abilities.keyword.ReachAbility;

/**
 *
 * @author spjspj
 */
public final class PenumbraSpiderToken extends TokenImpl {

    public PenumbraSpiderToken() {

        super("Spider", "2/4 black Spider creature token with reach");
        cardType.add(CardType.CREATURE);
        color.setBlack(true);
        subtype.add(SubType.SPIDER);
        power = new MageInt(2);
        toughness = new MageInt(4);
        addAbility(ReachAbility.getInstance());
    }

    public PenumbraSpiderToken(final PenumbraSpiderToken token) {
        super(token);
    }

    public PenumbraSpiderToken copy() {
        return new PenumbraSpiderToken(this);
    }
}
