
package mage.cards.f;

import java.util.UUID;

import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.ChooseOpponentEffect;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.cards.Card;
import mage.constants.*;
import mage.counters.Counter;
import mage.counters.CounterType;
import mage.filter.common.FilterCreatureCard;
import mage.filter.predicate.mageobject.ConvertedManaCostPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetCardInHand;
import mage.target.common.TargetCreaturePermanent;
import mage.abilities.Ability;
import mage.abilities.effects.common.AttachEffect;
import mage.target.TargetPermanent;
import mage.abilities.keyword.EnchantAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;

/**
 *
 * @author ciaccona007
 */
public final class FesteringWound extends CardImpl {

    public FesteringWound(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{1}{B}");
        
        this.subtype.add(SubType.AURA);

        // Enchant creature
        TargetPermanent auraTarget = new TargetCreaturePermanent();
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.BoostCreature));
        Ability ability = new EnchantAbility(auraTarget.getTargetName());
        this.addAbility(ability);

        // At the beginning of your upkeep, you may put an infection counter on Festering Wound.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(new AddCountersSourceEffect(CounterType.INFECTION.createInstance(), true), TargetController.YOU, true));
        // At the beginning of the upkeep of enchanted creature's controller, Festering Wound deals X damage to that player, where X is the number of infection counters on Festering Wound.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(Zone.BATTLEFIELD, new FesteringWoundEffect(), TargetController.CONTROLLER_ATTACHED_TO, false, true));
    }

    public FesteringWound(final FesteringWound card) {
        super(card);
    }

    @Override
    public FesteringWound copy() {
        return new FesteringWound(this);
    }
}
class FesteringWoundEffect extends OneShotEffect {

    public FesteringWoundEffect() {
        super(Outcome.Detriment);
        this.staticText = "{this} deals X damage to that player, where X is the number of infection counters on {this}";
    }

    public FesteringWoundEffect(final FesteringWoundEffect effect) {
        super(effect);
    }

    @Override
    public FesteringWoundEffect copy() {
        return new FesteringWoundEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        UUID sourceId = source.getSourceId();
        int amount = game.getPermanent(sourceId).getCounters(game).getCount(CounterType.INFECTION);
        UUID id = this.getTargetPointer().getFirst(game, source);
        Player player = game.getPlayer(id);
        if(player != null) {
            player.damage(amount, source.getSourceId(), game, false, true);
            return true;
        }
        return false;
    }
}
