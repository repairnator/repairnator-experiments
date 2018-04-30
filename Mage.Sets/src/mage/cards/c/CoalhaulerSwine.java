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
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DealtDamageToSourceTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.players.Player;

/**
 *
 * @author vereena42
 */
public class CoalhaulerSwine extends CardImpl {

    public CoalhaulerSwine(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{4}{R}{R}");

        this.subtype.add(SubType.BOAR);
        this.subtype.add(SubType.BEAST);
        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Whenever Coalhauler Swine is dealt damage, it deals that much damage to each player.
        this.addAbility(new DealtDamageToSourceTriggeredAbility(Zone.BATTLEFIELD, new CoalhaulerSwineEffect(), false, false, true));
    }

    public CoalhaulerSwine(final CoalhaulerSwine card) {
        super(card);
    }

    @Override
    public CoalhaulerSwine copy() {
        return new CoalhaulerSwine(this);
    }

    static class CoalhaulerSwineEffect extends OneShotEffect {

        public CoalhaulerSwineEffect() {
            super(Outcome.Damage);
            staticText = "it deals that much damage to each player";
        }

        public CoalhaulerSwineEffect(final CoalhaulerSwineEffect effect) {
            super(effect);
        }

        @Override
        public CoalhaulerSwineEffect copy() {
            return new CoalhaulerSwineEffect(this);
        }

        @Override
        public boolean apply(Game game, Ability source) {
            for (UUID playerId : game.getPlayers().keySet()) {
                Player player = game.getPlayer(playerId);
                if (player != null) {
                    player.damage((Integer) this.getValue("damage"), source.getSourceId(), game, false, true);
                }
            }
            return true;
        }

    }
}
