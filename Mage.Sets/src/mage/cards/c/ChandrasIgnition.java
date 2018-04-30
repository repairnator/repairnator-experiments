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
package mage.cards.c;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetControlledCreaturePermanent;

/**
 *
 * @author LevelX2
 */
public class ChandrasIgnition extends CardImpl {

    public ChandrasIgnition(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{3}{R}{R}");

        // Target creature you control deals damage equal to its power to each other creature and each opponent.
        this.getSpellAbility().addEffect(new ChandrasIgnitionEffect());
        this.getSpellAbility().addTarget(new TargetControlledCreaturePermanent());
    }

    public ChandrasIgnition(final ChandrasIgnition card) {
        super(card);
    }

    @Override
    public ChandrasIgnition copy() {
        return new ChandrasIgnition(this);
    }
}

class ChandrasIgnitionEffect extends OneShotEffect {

    public ChandrasIgnitionEffect() {
        super(Outcome.Benefit);
        this.staticText = "Target creature you control deals damage equal to its power to each other creature and each opponent";
    }

    public ChandrasIgnitionEffect(final ChandrasIgnitionEffect effect) {
        super(effect);
    }

    @Override
    public ChandrasIgnitionEffect copy() {
        return new ChandrasIgnitionEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent targetCreature = game.getPermanent(getTargetPointer().getFirst(game, source));
        if (targetCreature != null && targetCreature.getPower().getValue() > 0) {
            for (Permanent creature : game.getState().getBattlefield().getActivePermanents(StaticFilters.FILTER_PERMANENT_CREATURE, source.getControllerId(), source.getSourceId(), game)) {
                if (!creature.getId().equals(targetCreature.getId())) {
                    creature.damage(targetCreature.getPower().getValue(), targetCreature.getId(), game, false, true);
                }
            }
            for (UUID opponentId : game.getOpponents(source.getControllerId())) {
                Player opponent = game.getPlayer(opponentId);
                if (opponent != null) {
                    opponent.damage(targetCreature.getPower().getValue(), targetCreature.getId(), game, false, true);
                }
            }
        }
        return true;
    }
}
