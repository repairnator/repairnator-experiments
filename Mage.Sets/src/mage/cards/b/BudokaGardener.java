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
package mage.cards.b;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.FlipSourceEffect;
import mage.abilities.effects.common.PutLandFromHandOntoBattlefieldEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.common.FilterControlledLandPermanent;
import mage.filter.common.FilterControlledPermanent;
import mage.game.Game;
import mage.game.permanent.token.DokaiWeaverofLifeToken;
import mage.game.permanent.token.TokenImpl;
import mage.game.permanent.token.Token;
import mage.players.Player;

import java.util.UUID;

/**
 * @author Loki
 */
public class BudokaGardener extends CardImpl {

    public BudokaGardener(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{G}");
        this.subtype.add(SubType.HUMAN, SubType.MONK);

        this.power = new MageInt(2);
        this.toughness = new MageInt(1);
        this.flipCard = true;
        this.flipCardName = "Dokai, Weaver of Life";

        // {T}: You may put a land card from your hand onto the battlefield. If you control ten or more lands, flip Budoka Gardener.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new PutLandFromHandOntoBattlefieldEffect(), new TapSourceCost());
        ability.addEffect(new BudokaGardenerEffect());
        this.addAbility(ability);
    }

    public BudokaGardener(final BudokaGardener card) {
        super(card);
    }

    @Override
    public BudokaGardener copy() {
        return new BudokaGardener(this);
    }

}

class BudokaGardenerEffect extends OneShotEffect {

    final static FilterControlledPermanent filterLands = new FilterControlledLandPermanent("lands you control");

    BudokaGardenerEffect() {
        super(Outcome.PutLandInPlay);
        staticText = "If you control ten or more lands, flip {this}";
    }

    BudokaGardenerEffect(final BudokaGardenerEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            if (game.getBattlefield().count(filterLands, source.getSourceId(), source.getControllerId(), game) >= 10) {
                new FlipSourceEffect(new DokaiWeaverofLife()).apply(game, source);
            }
            return true;
        }
        return false;
    }

    @Override
    public BudokaGardenerEffect copy() {
        return new BudokaGardenerEffect(this);
    }

}

class DokaiWeaverofLife extends TokenImpl {

    DokaiWeaverofLife() {
        super("Dokai, Weaver of Life", "");
        addSuperType(SuperType.LEGENDARY);
        cardType.add(CardType.CREATURE);
        color.setGreen(true);
        subtype.add(SubType.HUMAN, SubType.MONK);
        power = new MageInt(3);
        toughness = new MageInt(3);

        // {4}{G}{G}, {T}: Create an X/X green Elemental creature token, where X is the number of lands you control.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new CreateTokenEffect(new DokaiWeaverofLifeToken()), new ManaCostsImpl("{4}{G}{G}"));
        ability.addCost(new TapSourceCost());
        this.addAbility(ability);
    }
    public DokaiWeaverofLife(final DokaiWeaverofLife token) {
        super(token);
    }

    public DokaiWeaverofLife copy() {
        return new DokaiWeaverofLife(this);
    }
}
