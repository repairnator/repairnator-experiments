
package mage.cards.a;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.continuous.GainAbilityTargetEffect;
import mage.abilities.keyword.GraftAbility;
import mage.abilities.keyword.ReachAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.CounterPredicate;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author JotaPeRL
 */
public final class AquastrandSpider extends CardImpl {
    
    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("creature with a +1/+1 counter on it");
    static {
        filter.add(new CounterPredicate(CounterType.P1P1)); 
    }

    public AquastrandSpider(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{1}{G}");
        this.subtype.add(SubType.SPIDER);
        this.subtype.add(SubType.MUTANT);
        this.power = new MageInt(0);
        this.toughness = new MageInt(0);

        // Graft 2
        this.addAbility(new GraftAbility(this, 2));
        
        // {G}: Target creature with a +1/+1 counter on it gains reach until end of turn.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new GainAbilityTargetEffect(ReachAbility.getInstance(), Duration.EndOfTurn), new ManaCostsImpl("{G}"));
        ability.addTarget(new TargetCreaturePermanent(filter));
        this.addAbility(ability);        
    }

    public AquastrandSpider(final AquastrandSpider card) {
        super(card);
    }

    @Override
    public AquastrandSpider copy() {
        return new AquastrandSpider(this);
    }
}
