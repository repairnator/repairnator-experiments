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
package mage.cards.s;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.LookLibraryControllerEffect;
import mage.abilities.effects.common.LookLibraryTopCardTargetPlayerEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.Cards;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.other.FaceDownPredicate;
import mage.filter.predicate.permanent.ControllerIdPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.TargetPlayer;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author L_J
 */
public class SpyNetwork extends CardImpl {

    public SpyNetwork(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{U}");

        // Look at target player's hand, the top card of that player's library, and any face-down creatures he or she controls. Look at the top four cards of your library, then put them back in any order.
        this.getSpellAbility().addEffect(new SpyNetworkLookAtTargetPlayerHandEffect());
        this.getSpellAbility().addEffect(new LookLibraryTopCardTargetPlayerEffect().setText(" the top card of that player's library"));
        this.getSpellAbility().addEffect(new SpyNetworkFaceDownEffect());
        this.getSpellAbility().addEffect(new LookLibraryControllerEffect(4, false, true));
        this.getSpellAbility().addTarget(new TargetPlayer());
    }

    public SpyNetwork(final SpyNetwork card) {
        super(card);
    }

    @Override
    public SpyNetwork copy() {
        return new SpyNetwork(this);
    }

}

class SpyNetworkLookAtTargetPlayerHandEffect extends OneShotEffect {

    public SpyNetworkLookAtTargetPlayerHandEffect() {
        super(Outcome.Benefit);
        this.staticText = "Look at target player's hand,";
    }

    public SpyNetworkLookAtTargetPlayerHandEffect(final SpyNetworkLookAtTargetPlayerHandEffect effect) {
        super(effect);
    }

    @Override
    public SpyNetworkLookAtTargetPlayerHandEffect copy() {
        return new SpyNetworkLookAtTargetPlayerHandEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player you = game.getPlayer(source.getControllerId());
        Player targetPlayer = game.getPlayer(source.getFirstTarget());
        MageObject sourceObject = game.getObject(source.getSourceId());
        if (you != null && targetPlayer != null) {
            you.lookAtCards("Hand of " + targetPlayer.getName() + " (" + (sourceObject != null ? sourceObject.getIdName() : null) + ')', targetPlayer.getHand(), game);
            return true;
        }
        return false;
    }

}

class SpyNetworkFaceDownEffect extends OneShotEffect {

    public SpyNetworkFaceDownEffect() {
        super(Outcome.Benefit);
        this.staticText = "and any face-down creatures he or she controls";
    }

    public SpyNetworkFaceDownEffect(final SpyNetworkFaceDownEffect effect) {
        super(effect);
    }

    @Override
    public SpyNetworkFaceDownEffect copy() {
        return new SpyNetworkFaceDownEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Player player = game.getPlayer(this.getTargetPointer().getFirst(game, source));
        MageObject mageObject = game.getObject(source.getSourceId());
        if (controller != null && player != null && mageObject != null) {
            FilterCreaturePermanent filter = new FilterCreaturePermanent("face down creature controlled by " + player.getLogName());
            filter.add(new FaceDownPredicate());
            filter.add(new ControllerIdPredicate(player.getId()));
            TargetCreaturePermanent target = new TargetCreaturePermanent(1, 1, filter, true);
            if (target.canChoose(source.getSourceId(), controller.getId(), game)) {
                while (player != null && controller.chooseUse(outcome, "Look at a face down creature controlled by " + player.getLogName() + "?", source, game)) {
                    target.clearChosen();
                    while (!target.isChosen() && target.canChoose(controller.getId(), game) && controller.canRespond()) {
                        controller.chooseTarget(outcome, target, source, game);
                    }
                    Permanent faceDownCreature = game.getPermanent(target.getFirstTarget());
                    if (faceDownCreature != null) {
                        Permanent copyFaceDown = faceDownCreature.copy();
                        copyFaceDown.setFaceDown(false, game);
                        Cards cards = new CardsImpl();
                        cards.add(copyFaceDown);
                        controller.lookAtCards("face down card - " + mageObject.getName(), cards, game);
                        if (player != null) {
                            game.informPlayers(controller.getLogName() + " looks at a face down creature controlled by " + player.getLogName());
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
