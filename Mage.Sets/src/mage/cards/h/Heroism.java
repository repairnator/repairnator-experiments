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
package mage.cards.h;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mage.ObjectColor;
import mage.abilities.Ability;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.Cost;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.ContinuousEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.PreventDamageByTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterAttackingCreature;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.mageobject.ColorPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetControlledCreaturePermanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author L_J
 */
public class Heroism extends CardImpl {

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent("a white creature");
    static {
        filter.add(new ColorPredicate(ObjectColor.WHITE));
    }

    public Heroism(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{2}{W}");
        
        // Sacrifice a white creature: For each attacking red creature, prevent all combat damage that would be dealt by that creature this turn unless its controller pays {2}{R}.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new HeroismEffect(), new SacrificeTargetCost(new TargetControlledCreaturePermanent(1,1, filter, true))));
    }

    public Heroism(final Heroism card) {
        super(card);
    }

    @Override
    public Heroism copy() {
        return new Heroism(this);
    }
}

class HeroismEffect extends OneShotEffect {
    
    private static final FilterAttackingCreature filter = new FilterAttackingCreature("attacking red creature");
    static {
        filter.add(new ColorPredicate(ObjectColor.RED));
    }

    public HeroismEffect() {
        super(Outcome.Benefit);
        this.staticText = "For each attacking red creature, prevent all combat damage that would be dealt by that creature this turn unless its controller pays {2}{R}";
    }

    public HeroismEffect(final HeroismEffect effect) {
        super(effect);
    }

    @Override
    public HeroismEffect copy() {
        return new HeroismEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        game.getPlayerList();
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Player player = game.getPlayer(game.getActivePlayerId());
            Cost cost = new ManaCostsImpl("{2}{R}");
            List<Permanent> permanentsToPrevent = new ArrayList<>();
            for (Permanent permanent : game.getState().getBattlefield().getAllActivePermanents(filter, player.getId(), game)) {
                cost.clearPaid();
                String message = "Pay " + cost.getText() + "? If you don't, " + permanent.getLogName() + "'s combat damage will be prevented this turn.";
                if (player != null && player.chooseUse(Outcome.Neutral, message, source, game)) {
                    if (cost.pay(source, game, source.getSourceId(), player.getId(), false, null)) {
                        game.informPlayers(player.getLogName() + " paid " + cost.getText() + " for " + permanent.getLogName());
                        continue;
                    } else {
                        game.informPlayers(player.getLogName() + " didn't pay " + cost.getText() + " for " + permanent.getLogName());
                        permanentsToPrevent.add(permanent);
                    }
                } else {
                    game.informPlayers(player.getLogName() + " didn't pay " + cost.getText() + " for " + permanent.getLogName());
                    permanentsToPrevent.add(permanent);
                }
            }

            for (Permanent permanent : permanentsToPrevent) {
                ContinuousEffect effect = new PreventDamageByTargetEffect(Duration.EndOfTurn, Integer.MAX_VALUE, true);
                effect.setTargetPointer(new FixedTarget(permanent.getId()));
                game.addEffect(effect, source);
            }
            return true;
        }
        return false;
    }
}
