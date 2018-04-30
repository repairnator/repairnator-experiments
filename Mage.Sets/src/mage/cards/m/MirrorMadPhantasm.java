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
package mage.cards.m;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.Cards;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;

/**
 *
 * @author BetaSteward
 */
public class MirrorMadPhantasm extends CardImpl {

    public MirrorMadPhantasm(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{3}{U}{U}");
        this.subtype.add(SubType.SPIRIT);

        this.power = new MageInt(5);
        this.toughness = new MageInt(1);

        this.addAbility(FlyingAbility.getInstance());
        // {1}{U}: Mirror-Mad Phantasm's owner shuffles it into their library. If that player does, he or she reveals cards from the top of that library until a card named Mirror-Mad Phantasm is revealed. That player puts that card onto the battlefield and all other cards revealed this way into their graveyard.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new MirrorMadPhantasmEffect(), new ManaCostsImpl("{1}{U}")));

    }

    public MirrorMadPhantasm(final MirrorMadPhantasm card) {
        super(card);
    }

    @Override
    public MirrorMadPhantasm copy() {
        return new MirrorMadPhantasm(this);
    }
}

class MirrorMadPhantasmEffect extends OneShotEffect {

    public MirrorMadPhantasmEffect() {
        super(Outcome.Detriment);
        this.staticText = "{this}'s owner shuffles it into their library. If that player does, he or she reveals cards from the top of that library until a card named Mirror-Mad Phantasm is revealed. That player puts that card onto the battlefield and all other cards revealed this way into their graveyard";
    }

    public MirrorMadPhantasmEffect(final MirrorMadPhantasmEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent perm = game.getPermanent(source.getSourceId());
        if (perm != null) {
            Player player = game.getPlayer(perm.getOwnerId());
            if (player != null) {
                perm.moveToZone(Zone.LIBRARY, source.getSourceId(), game, true);
                player.shuffleLibrary(source, game);
                Cards cards = new CardsImpl();
                while (player.getLibrary().hasCards()) {
                    Card card = player.getLibrary().removeFromTop(game);
                    if (card == null) {
                        break;
                    }
                    if (card.getName().equals("Mirror-Mad Phantasm")) {
                        player.moveCards(card, Zone.BATTLEFIELD, source, game);
                        break;
                    }
                    cards.add(card);
                }
                if (!cards.isEmpty()) {
                    player.revealCards("Mirror-Mad Phantasm", cards, game);
                    player.moveCards(cards, Zone.GRAVEYARD, source, game);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public MirrorMadPhantasmEffect copy() {
        return new MirrorMadPhantasmEffect(this);
    }

}
