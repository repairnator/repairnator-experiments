/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.w;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.players.Player;
import mage.target.common.TargetCardInHand;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX2
 */
public class WidespreadPanic extends CardImpl {

    public WidespreadPanic(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{2}{R}");

        // Whenever a spell or ability causes its controller to shuffle their library, that player puts a card from their hand on top of their library.
        this.addAbility(new WidespreadPanicTriggeredAbility());
    }

    public WidespreadPanic(final WidespreadPanic card) {
        super(card);
    }

    @Override
    public WidespreadPanic copy() {
        return new WidespreadPanic(this);
    }
}

class WidespreadPanicTriggeredAbility extends TriggeredAbilityImpl {

    public WidespreadPanicTriggeredAbility() {
        super(Zone.BATTLEFIELD, new WidespreadPanicEffect(), false);
    }

    public WidespreadPanicTriggeredAbility(final WidespreadPanicTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public WidespreadPanicTriggeredAbility copy() {
        return new WidespreadPanicTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.LIBRARY_SHUFFLED;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getPlayerId().equals(game.getControllerId(event.getSourceId()))) {
            for (Effect effect : this.getEffects()) {
                effect.setTargetPointer(new FixedTarget(event.getPlayerId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public String getRule() {
        return new StringBuilder("Whenever a spell or ability causes its controller to shuffle their library, ").append(super.getRule()).toString();
    }
}

class WidespreadPanicEffect extends OneShotEffect {

    public WidespreadPanicEffect() {
        super(Outcome.Detriment);
        this.staticText = "that player puts a card from their hand on top of their library";
    }

    public WidespreadPanicEffect(final WidespreadPanicEffect effect) {
        super(effect);
    }

    @Override
    public WidespreadPanicEffect copy() {
        return new WidespreadPanicEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player shuffler = game.getPlayer(this.getTargetPointer().getFirst(game, source));
        if (shuffler != null) {
            if (!shuffler.getHand().isEmpty()) {
                TargetCardInHand target = new TargetCardInHand();
                target.setNotTarget(true);
                target.setTargetName("a card from your hand to put on top of your library");
                shuffler.choose(Outcome.Detriment, target, source.getSourceId(), game);
                Card card = shuffler.getHand().get(target.getFirstTarget(), game);
                if (card != null) {
                    shuffler.moveCardToLibraryWithInfo(card, source.getSourceId(), game, Zone.HAND, true, false);

                }
            }
            return true;
        }
        return false;
    }
}
