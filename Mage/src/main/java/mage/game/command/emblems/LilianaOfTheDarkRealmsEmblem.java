
package mage.game.command.emblems;

import mage.Mana;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.common.continuous.GainAbilityControlledEffect;
import mage.abilities.mana.SimpleManaAbility;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.filter.common.FilterLandPermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.command.Emblem;

/**
 *
 * @author spjspj
 */
public class LilianaOfTheDarkRealmsEmblem extends Emblem {

    private static final FilterLandPermanent filter = new FilterLandPermanent("Swamps");

    static {
        filter.add(new SubtypePredicate(SubType.SWAMP));
    }

    public LilianaOfTheDarkRealmsEmblem() {
        this.setName("Emblem Liliana");
        SimpleManaAbility manaAbility = new SimpleManaAbility(Zone.BATTLEFIELD, Mana.BlackMana(4), new TapSourceCost());
        Ability ability = new SimpleStaticAbility(Zone.COMMAND, new GainAbilityControlledEffect(manaAbility, Duration.WhileOnBattlefield, filter));
        this.getAbilities().add(ability);
    }
}
