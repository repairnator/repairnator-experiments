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

package mage.cards.r;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.permanent.Permanent;

/**
 *
 * @author Loki
 */
public class RatchetBomb extends CardImpl {

    public RatchetBomb (UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT},"{2}");
        
        // {T}: Put a charge counter on Ratchet Bomb.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new AddCountersSourceEffect(CounterType.CHARGE.createInstance()), new TapSourceCost()));
        
        // {T}, Sacrifice Ratchet Bomb: Destroy each nonland permanent with a converted mana cost equal to the number of charge counters on Ratchet Bomb.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new RatchetBombEffect(), new TapSourceCost());
        ability.addCost(new SacrificeSourceCost());
        this.addAbility(ability);
    }

    public RatchetBomb (final RatchetBomb card) {
        super(card);
    }

    @Override
    public RatchetBomb copy() {
        return new RatchetBomb(this);
    }

    class RatchetBombEffect extends OneShotEffect {

        public RatchetBombEffect() {
            super(Outcome.DestroyPermanent);
            staticText = "Destroy each nonland permanent with converted mana cost equal to the number of charge counters on {this}";
        }

        public RatchetBombEffect(final RatchetBombEffect effect) {
            super(effect);
        }

        @Override
        public boolean apply(Game game, Ability source) {
            Permanent p = game.getBattlefield().getPermanent(source.getSourceId());
            if (p == null) {
                p = (Permanent) game.getLastKnownInformation(source.getSourceId(), Zone.BATTLEFIELD);
                if (p == null) {
                    return false;
                }
            }

            int count = p.getCounters(game).getCount(CounterType.CHARGE);
            for (Permanent perm: game.getBattlefield().getAllActivePermanents()) {
                if (perm.getConvertedManaCost() == count && !(perm.isLand())) {
                    perm.destroy(source.getSourceId(), game, false);
                }
            }

            return true;
        }

        @Override
        public RatchetBombEffect copy() {
            return new RatchetBombEffect(this);
        }

    }

}
