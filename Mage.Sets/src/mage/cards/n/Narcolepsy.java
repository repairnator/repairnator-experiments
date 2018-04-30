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
package mage.cards.n;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.AttachEffect;
import mage.abilities.keyword.EnchantAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.TargetPermanent;

/**
 *
 * @author jeffwadsworth
 */
public class Narcolepsy extends CardImpl {


    public Narcolepsy(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{1}{U}");
        this.subtype.add(SubType.AURA);


        // Enchant creature
        TargetPermanent auraTarget = new TargetPermanent(StaticFilters.FILTER_PERMANENT_CREATURE);
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.Detriment));
        EnchantAbility ability = new EnchantAbility(auraTarget.getTargetName());
        this.addAbility(ability);

        // At the beginning of each upkeep, if enchanted creature is untapped, tap it.
        this.addAbility(new NarcolepsyTriggeredAbility());
    }

    public Narcolepsy(final Narcolepsy card) {
        super(card);
    }

    @Override
    public Narcolepsy copy() {
        return new Narcolepsy(this);
    }
}

class NarcolepsyTriggeredAbility extends BeginningOfUpkeepTriggeredAbility {
    
    NarcolepsyTriggeredAbility() {
        super(new NarcolepsyEffect(), TargetController.ANY, false);
    }
    
    NarcolepsyTriggeredAbility(final NarcolepsyTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public boolean checkInterveningIfClause(Game game) {
        Permanent narcolepsy = game.getPermanent(this.getSourceId());
        if (narcolepsy != null) {
            Permanent enchanted = game.getPermanent(narcolepsy.getAttachedTo());
            if (enchanted != null && !enchanted.isTapped()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public NarcolepsyTriggeredAbility copy() {
        return new NarcolepsyTriggeredAbility(this);
    }
    
    @Override
    public String getRule() {
        return "At the beginning of each upkeep, if enchanted creature is untapped, tap it.";
    }
}

class NarcolepsyEffect extends OneShotEffect {

    NarcolepsyEffect() {
        super(Outcome.Tap);
    }

    NarcolepsyEffect(final NarcolepsyEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent narcolepsy = game.getPermanent(source.getSourceId());
        if (narcolepsy != null) {
            Permanent enchanted = game.getPermanent(narcolepsy.getAttachedTo());
            if (enchanted != null) {
                enchanted.tap(game);
                return true;
            }
        }
        return false;
    }

    @Override
    public NarcolepsyEffect copy() {
        return new NarcolepsyEffect(this);
    }
}
