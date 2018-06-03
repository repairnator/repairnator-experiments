
package mage.cards.b;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.ActivateIfConditionActivatedAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.condition.common.PermanentsOnTheBattlefieldCondition;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.TransformSourceEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.abilities.keyword.TransformAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.l.LordOfLineage;
import mage.constants.CardType;
import mage.constants.ComparisonType;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.filter.common.FilterControlledPermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.permanent.token.VampireToken;

/**
 *
 * @author Loki
 */
public final class BloodlineKeeper extends CardImpl {

    private static final FilterControlledPermanent filter = new FilterControlledPermanent("you control five or more Vampires");

    static {
        filter.add(new SubtypePredicate(SubType.VAMPIRE));
    }

    public BloodlineKeeper(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{2}{B}{B}");
        this.subtype.add(SubType.VAMPIRE);

        this.power = new MageInt(3);
        this.toughness = new MageInt(3);

        this.transformable = true;
        this.secondSideCardClazz = LordOfLineage.class;

        this.addAbility(FlyingAbility.getInstance());
        // {T}: Create a 2/2 black Vampire creature token with flying.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new CreateTokenEffect(new VampireToken()), new TapSourceCost()));
        // {B}: Transform Bloodline Keeper. Activate this ability only if you control five or more Vampires.
        this.addAbility(new TransformAbility());
        Ability ability = new ActivateIfConditionActivatedAbility(Zone.BATTLEFIELD,
                new TransformSourceEffect(true),
                new ManaCostsImpl("{B}"),
                new PermanentsOnTheBattlefieldCondition(filter, ComparisonType.MORE_THAN, 4));
        this.addAbility(ability);
    }

    public BloodlineKeeper(final BloodlineKeeper card) {
        super(card);
    }

    @Override
    public BloodlineKeeper copy() {
        return new BloodlineKeeper(this);
    }

}
