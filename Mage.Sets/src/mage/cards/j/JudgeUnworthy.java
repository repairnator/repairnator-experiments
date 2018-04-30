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
package mage.cards.j;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.keyword.ScryEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetAttackingOrBlockingCreature;

/**
 *
 * @author fireshoes
 */
public class JudgeUnworthy extends CardImpl {

    public JudgeUnworthy(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{1}{W}");

        // Choose target attacking or blocking creature. Scry 3, then reveal the top card of your library. Judge Unworthy deals damage equal to that card's converted mana cost to that creature.
        this.getSpellAbility().addTarget(new TargetAttackingOrBlockingCreature());
        Effect effect = new ScryEffect(3);
        effect.setText("Choose target attacking or blocking creature. Scry 3");
        this.getSpellAbility().addEffect(effect);
        this.getSpellAbility().addEffect(new JudgeUnworthyEffect());  
    }

    public JudgeUnworthy(final JudgeUnworthy card) {
        super(card);
    }

    @Override
    public JudgeUnworthy copy() {
        return new JudgeUnworthy(this);
    }
}

class JudgeUnworthyEffect extends OneShotEffect {
    
    public JudgeUnworthyEffect() {
        super(Outcome.Damage);
        this.staticText = ", then reveal the top card of your library. {this} deals damage equal to that card's converted mana cost to that creature";
    }
    
    public JudgeUnworthyEffect(final JudgeUnworthyEffect effect) {
        super(effect);
    }
    
    @Override
    public JudgeUnworthyEffect copy() {
        return new JudgeUnworthyEffect(this);
    }
    
    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Card sourceCard = game.getCard(source.getSourceId());
        if (sourceCard != null && controller != null) {
            if (controller.getLibrary().hasCards()) {
                Card card = controller.getLibrary().getFromTop(game);
                controller.revealCards(sourceCard.getName(), new CardsImpl(card), game);
                Permanent targetCreature = game.getPermanent(this.getTargetPointer().getFirst(game, source));
                if (targetCreature != null) {
                    targetCreature.damage(card.getConvertedManaCost(), source.getSourceId(), game, false, true);
                    return true;
                }
            }
            return true;
        }
        return false;
    }
}
