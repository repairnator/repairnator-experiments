

package mage.game.permanent.token;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.MageInt;

/**
 *
 * @author spjspj
 */
public final class EldraziToken extends TokenImpl {

    public EldraziToken() {
        super("Eldrazi", "10/10 colorless Eldrazi creature token");
        cardType.add(CardType.CREATURE);
        subtype.add(SubType.ELDRAZI);
        power = new MageInt(10);
        toughness = new MageInt(10);
    }

    public EldraziToken(final EldraziToken token) {
        super(token);
    }

    public EldraziToken copy() {
        return new EldraziToken(this);
    }
}
