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
package mage.cards.d;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetLandPermanent;

/**
 *
 * @author North
 */
public class DesecratedEarth extends CardImpl {

    public DesecratedEarth(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{4}{B}");

        // Destroy target land. Its controller discards a card.
        this.getSpellAbility().addTarget(new TargetLandPermanent());
        this.getSpellAbility().addEffect(new DestroyTargetEffect());
        this.getSpellAbility().addEffect(new DesecratedEarthEffect());

    }

    public DesecratedEarth(final DesecratedEarth card) {
        super(card);
    }

    @Override
    public DesecratedEarth copy() {
        return new DesecratedEarth(this);
    }
}

class DesecratedEarthEffect extends OneShotEffect {

    public DesecratedEarthEffect() {
        super(Outcome.DrawCard);
        this.staticText = "Its controller discards a card";
    }

    public DesecratedEarthEffect(final DesecratedEarthEffect effect) {
        super(effect);
    }

    @Override
    public DesecratedEarthEffect copy() {
        return new DesecratedEarthEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = game.getPermanentOrLKIBattlefield(getTargetPointer().getFirst(game, source));
        if (permanent != null) {
            Player player = game.getPlayer(permanent.getControllerId());
            if (player != null) {
                player.discard(1, false, source, game);
                return true;
            }
        }
        return false;
    }
}
