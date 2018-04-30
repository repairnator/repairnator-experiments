package mage.game.permanent.token;

import mage.MageInt;
import mage.ObjectColor;
import mage.abilities.keyword.MenaceAbility;
import mage.constants.CardType;
import mage.constants.SubType;

public class ZombieKnightToken extends TokenImpl {

    public ZombieKnightToken(){
        super("Zombie Knight", "a 2/2 black Zombie Knight creature token with menace");
        color.addColor(ObjectColor.BLACK);
        cardType.add(CardType.CREATURE);
        subtype.add(SubType.ZOMBIE, SubType.KNIGHT);
        addAbility(new MenaceAbility());
        power = new MageInt(2);
        toughness = new MageInt(2);
    }

    public ZombieKnightToken(final ZombieKnightToken zombieKnightToken){
        super(zombieKnightToken);
    }

    @Override
    public ZombieKnightToken copy() {
        return new ZombieKnightToken(this);
    }
}
