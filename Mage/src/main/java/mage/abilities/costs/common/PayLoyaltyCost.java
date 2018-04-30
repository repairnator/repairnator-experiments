/*
* Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
*    1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*    2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and should not be interpreted as representing official policies, either expressed
* or implied, of BetaSteward_at_googlemail.com.
 */
package mage.abilities.costs.common;

import mage.abilities.Ability;
import mage.abilities.costs.Cost;
import mage.abilities.costs.CostImpl;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.permanent.Permanent;

import java.util.UUID;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class PayLoyaltyCost extends CostImpl {

    private final int amount;

    public PayLoyaltyCost(int amount) {
        this.amount = amount;
        this.text = Integer.toString(amount);
        if (amount > 0) {
            this.text = '+' + this.text;
        }
    }

    public PayLoyaltyCost(PayLoyaltyCost cost) {
        super(cost);
        this.amount = cost.amount;
    }

    @Override
    public boolean canPay(Ability ability, UUID sourceId, UUID controllerId, Game game) {
        Permanent planeswalker = game.getPermanent(sourceId);
        return planeswalker != null && planeswalker.getCounters(game).getCount(CounterType.LOYALTY) + amount >= 0 && planeswalker.canLoyaltyBeUsed(game);
    }

    /**
    * Gatherer Ruling:
    * 10/1/2005: Planeswalkers will enter the battlefield with double the normal amount of loyalty counters. However,
    * if you activate an ability whose cost has you put loyalty counters on a planeswalker, the number you put on isn’t doubled.
    * This is because those counters are put on as a cost, not as an effect.
    **/
    @Override
    public boolean pay(Ability ability, Game game, UUID sourceId, UUID controllerId, boolean noMana, Cost costToPay) {
        Permanent planeswalker = game.getPermanent(sourceId);
        if (planeswalker != null && planeswalker.getCounters(game).getCount(CounterType.LOYALTY) + amount >= 0 && planeswalker.canLoyaltyBeUsed(game)) {
            if (amount > 0) {
                planeswalker.getCounters(game).addCounter(CounterType.LOYALTY.createInstance(amount));
            } else if (amount < 0) {
                planeswalker.removeCounters(CounterType.LOYALTY.getName(), Math.abs(amount), game);
            }
            planeswalker.addLoyaltyUsed();
            this.paid = true;
        }
        return paid;
    }

    @Override
    public PayLoyaltyCost copy() {
        return new PayLoyaltyCost(this);
    }

}
