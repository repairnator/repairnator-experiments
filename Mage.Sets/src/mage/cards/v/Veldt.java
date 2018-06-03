
package mage.cards.v;

import java.util.UUID;
import mage.Mana;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.condition.common.SourceHasCounterCondition;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.decorator.ConditionalContinuousRuleModifyingEffect;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.DontUntapInControllersUntapStepSourceEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.effects.common.counter.RemoveCounterSourceEffect;
import mage.abilities.mana.SimpleManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.counters.CounterType;

/**
 *
 * @author Luna Skyrise
 */
public final class Veldt extends CardImpl {

    public Veldt(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.LAND},"");

        // Veldt doesn't untap during your untap step if it has a depletion counter on it.
        Effect effect = new ConditionalContinuousRuleModifyingEffect(new DontUntapInControllersUntapStepSourceEffect(false, true),
                new SourceHasCounterCondition(CounterType.DEPLETION, 1, Integer.MAX_VALUE));
        effect.setText("{this} doesn't untap during your untap step if it has a depletion counter on it");
        Ability ability = new SimpleStaticAbility(Zone.BATTLEFIELD, effect);
        this.addAbility(ability);
        // At the beginning of your upkeep, remove a depletion counter from Veldt.
        Ability ability2 = new BeginningOfUpkeepTriggeredAbility(new RemoveCounterSourceEffect(CounterType.DEPLETION.createInstance()), TargetController.YOU, false);
        this.addAbility(ability2);
        // {tap}: Add {G} or {W}. Put a depletion counter on Veldt.
        Ability ability3 = new SimpleManaAbility(Zone.BATTLEFIELD, Mana.GreenMana(1), new TapSourceCost());
        ability3.addEffect(new AddCountersSourceEffect(CounterType.DEPLETION.createInstance()));
        this.addAbility(ability3);
        Ability ability4 = new SimpleManaAbility(Zone.BATTLEFIELD, Mana.WhiteMana(1), new TapSourceCost());
        ability4.addEffect(new AddCountersSourceEffect(CounterType.DEPLETION.createInstance()));
        this.addAbility(ability4);
    }

    public Veldt(final Veldt card) {
        super(card);
    }

    @Override
    public Veldt copy() {
        return new Veldt(this);
    }
}
