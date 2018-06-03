

package mage.game.permanent.token;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.MageInt;
import mage.abilities.keyword.HasteAbility;
import mage.abilities.keyword.FlyingAbility;

/**
 *
 * @author spjspj
 */
public final class TheLocustGodInsectToken extends TokenImpl {

    public TheLocustGodInsectToken() {
        super("Insect", "1/1 blue and red Insect creature token with flying and haste");
        setOriginalExpansionSetCode("HOU");
        cardType.add(CardType.CREATURE);
        color.setBlue(true);
        color.setRed(true);
        subtype.add(SubType.INSECT);
        power = new MageInt(1);
        toughness = new MageInt(1);
        addAbility(FlyingAbility.getInstance());
        addAbility(HasteAbility.getInstance());
    }

    public TheLocustGodInsectToken(final TheLocustGodInsectToken token) {
        super(token);
    }

    public TheLocustGodInsectToken copy() {
        return new TheLocustGodInsectToken(this);
    }
}
