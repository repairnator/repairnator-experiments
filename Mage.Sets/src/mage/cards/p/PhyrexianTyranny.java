
package mage.cards.p;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.costs.Cost;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.players.Player;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author emerald000
 */
public final class PhyrexianTyranny extends CardImpl {

    public PhyrexianTyranny(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{U}{B}{R}");

        // Whenever a player draws a card, that player loses 2 life unless he or she pays {2}.
        this.addAbility(new PhyrexianTyrannyTriggeredAbility());
    }

    public PhyrexianTyranny(final PhyrexianTyranny card) {
        super(card);
    }

    @Override
    public PhyrexianTyranny copy() {
        return new PhyrexianTyranny(this);
    }
}

class PhyrexianTyrannyTriggeredAbility extends TriggeredAbilityImpl {

    PhyrexianTyrannyTriggeredAbility() {
        super(Zone.BATTLEFIELD, new PhyrexianTyrannyEffect(), false);
    }

    PhyrexianTyrannyTriggeredAbility(final PhyrexianTyrannyTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public PhyrexianTyrannyTriggeredAbility copy() {
        return new PhyrexianTyrannyTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DREW_CARD;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        for (Effect effect : this.getEffects()) {
            if (effect instanceof PhyrexianTyrannyEffect) {
                effect.setTargetPointer(new FixedTarget(event.getPlayerId()));
            }
        }
        return true;
    }

    @Override
    public String getRule() {
        return "Whenever a player draws a card, that player loses 2 life unless he or she pays {2}.";
    }
}

class PhyrexianTyrannyEffect extends OneShotEffect {

    PhyrexianTyrannyEffect() {
        super(Outcome.Neutral);
        this.staticText = "that player loses 2 life unless he or she pays {2}";
    }

    PhyrexianTyrannyEffect(final PhyrexianTyrannyEffect effect) {
        super(effect);
    }

    @Override
    public PhyrexianTyrannyEffect copy() {
        return new PhyrexianTyrannyEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(targetPointer.getFirst(game, source));
        if (player != null) {
            Cost cost = new GenericManaCost(2);
            if (!cost.pay(source, game, player.getId(), player.getId(), false, null)) {
                player.loseLife(2, game, false);
            }
            return true;
        }
        return false;
    }
}
