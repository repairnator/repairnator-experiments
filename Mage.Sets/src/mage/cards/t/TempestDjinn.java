
package mage.cards.t;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.constants.SubType;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.filter.common.FilterControlledPermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.filter.predicate.mageobject.SupertypePredicate;

/**
 *
 * @author TheElk801
 */
public final class TempestDjinn extends CardImpl {

    private static final FilterControlledPermanent filter = new FilterControlledPermanent("basic Island you control");

    static {
        filter.add(new SupertypePredicate(SuperType.BASIC));
        filter.add(new SubtypePredicate(SubType.ISLAND));
    }

    public TempestDjinn(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{U}{U}{U}");

        this.subtype.add(SubType.DJINN);
        this.power = new MageInt(0);
        this.toughness = new MageInt(4);

        // Flying
        this.addAbility(FlyingAbility.getInstance());

        // Tempest Djinn gets +1/+0 for each basic Island you control.
        PermanentsOnBattlefieldCount count = new PermanentsOnBattlefieldCount(filter);
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new BoostSourceEffect(count, new StaticValue(0), Duration.WhileOnBattlefield)));
    }

    public TempestDjinn(final TempestDjinn card) {
        super(card);
    }

    @Override
    public TempestDjinn copy() {
        return new TempestDjinn(this);
    }
}
