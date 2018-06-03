
package mage.cards.a;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.LoyaltyAbility;
import mage.abilities.common.PlanswalkerEntersWithLoyalityCountersAbility;
import mage.abilities.effects.common.GainLifeEffect;
import mage.abilities.effects.common.LookLibraryAndPickControllerEffect;
import mage.abilities.effects.common.counter.DistributeCountersEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.filter.FilterCard;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.target.common.TargetCreaturePermanentAmount;

/**
 *
 * @author LevelX2
 */
public final class AjaniMentorOfHeroes extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("creatures you control");
    private static final FilterCard filterCard = new FilterCard("an Aura, creature, or planeswalker card");

    static {
        filter.add(new ControllerPredicate(TargetController.YOU));
        filterCard.add(Predicates.or(
                new SubtypePredicate(SubType.AURA),
                new CardTypePredicate(CardType.CREATURE),
                new CardTypePredicate(CardType.PLANESWALKER)));
    }

    public AjaniMentorOfHeroes(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.PLANESWALKER},"{3}{G}{W}");
        this.addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.AJANI);

        this.addAbility(new PlanswalkerEntersWithLoyalityCountersAbility(4));

        // +1: Distribute three +1/+1 counters among one, two, or three target creatures you control
        Ability ability = new LoyaltyAbility(new DistributeCountersEffect(CounterType.P1P1, 3, false, "one, two, or three target creatures you control"), 1);
        ability.addTarget(new TargetCreaturePermanentAmount(3, filter));
        this.addAbility(ability);

        // +1: Look at the top four cards of your library. You may reveal an Aura, creature, or planeswalker card from among them and put that card into your hand. Put the rest on the bottom of your library in any order.
        this.addAbility(new LoyaltyAbility(new LookLibraryAndPickControllerEffect(4, 1, filterCard, true, false, Zone.HAND, true), 1));

        // -8: You gain 100 life.
        this.addAbility(new LoyaltyAbility(new GainLifeEffect(100), -8));
    }

    public AjaniMentorOfHeroes(final AjaniMentorOfHeroes card) {
        super(card);
    }

    @Override
    public AjaniMentorOfHeroes copy() {
        return new AjaniMentorOfHeroes(this);
    }
}
