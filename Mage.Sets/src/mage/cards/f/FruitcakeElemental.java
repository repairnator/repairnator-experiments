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
package mage.cards.f;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfEndStepTriggeredAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.ContinuousEffect;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.DamageControllerEffect;
import mage.abilities.keyword.IndestructibleAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Layer;
import mage.constants.Outcome;
import mage.constants.SubLayer;
import mage.constants.SubType;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.TargetPlayer;

/**
 *
 * @author spjspj
 */
public class FruitcakeElemental extends CardImpl {

    public FruitcakeElemental(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{G}{G}");

        this.subtype.add(SubType.ELEMENTAL);
        this.power = new MageInt(7);
        this.toughness = new MageInt(7);

        // Fruitcake Elemental is indestructible.
        this.addAbility(IndestructibleAbility.getInstance());

        // At the end of your turn, Fruitcake Elemental deals 7 damage to you.
        this.addAbility(new BeginningOfEndStepTriggeredAbility(new DamageControllerEffect(7), TargetController.YOU, false));

        // {3}: Target player gains control of Fruitcake Elemental.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new FruitcakeElementalEffect(), new ManaCostsImpl("{3}"));
        ability.addTarget(new TargetPlayer(1, 1, false));
        this.addAbility(ability);
    }

    public FruitcakeElemental(final FruitcakeElemental card) {
        super(card);
    }

    @Override
    public FruitcakeElemental copy() {
        return new FruitcakeElemental(this);
    }
}

class FruitcakeElementalEffect extends OneShotEffect {

    public FruitcakeElementalEffect() {
        super(Outcome.Discard);
        this.staticText = "Target player gains control of {this}.";
    }

    public FruitcakeElementalEffect(final FruitcakeElementalEffect effect) {
        super(effect);
    }

    @Override
    public FruitcakeElementalEffect copy() {
        return new FruitcakeElementalEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = (Permanent) source.getSourceObjectIfItStillExists(game);
        Player player = game.getPlayer(getTargetPointer().getFirst(game, source));
        if (player != null && permanent != null) {
            ContinuousEffect effect = new FruitcakeElementalControlSourceEffect();
            game.addEffect(effect, source);
            game.informPlayers(permanent.getName() + " is now controlled by " + player.getLogName());
            return true;
        }
        return false;
    }
}

class FruitcakeElementalControlSourceEffect extends ContinuousEffectImpl {

    public FruitcakeElementalControlSourceEffect() {
        super(Duration.Custom, Layer.ControlChangingEffects_2, SubLayer.NA, Outcome.GainControl);
    }

    public FruitcakeElementalControlSourceEffect(final FruitcakeElementalControlSourceEffect effect) {
        super(effect);
    }

    @Override
    public FruitcakeElementalControlSourceEffect copy() {
        return new FruitcakeElementalControlSourceEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getFirstTarget());
        Permanent permanent = game.getPermanent(source.getSourceId());
        if (permanent != null && player != null) {
            permanent.changeControllerId(player.getId(), game);
        } else {
            // no valid target exists, effect can be discarded
            discard();
        }
        return true;
    }
}
