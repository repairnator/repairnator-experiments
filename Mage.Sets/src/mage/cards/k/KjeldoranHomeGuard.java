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
package mage.cards.k;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EndOfCombatTriggeredAbility;
import mage.abilities.condition.common.AttackedOrBlockedThisCombatSourceCondition;
import mage.abilities.decorator.ConditionalTriggeredAbility;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.constants.SubType;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.counters.BoostCounter;
import mage.game.permanent.token.DeserterToken;
import mage.watchers.common.AttackedOrBlockedThisCombatWatcher;

/**
 *
 * @author TheElk801
 */
public class KjeldoranHomeGuard extends CardImpl {

    public KjeldoranHomeGuard(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{3}{W}");

        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.SOLDIER);
        this.power = new MageInt(1);
        this.toughness = new MageInt(6);

        // At end of combat, if Kjeldoran Home Guard attacked or blocked this combat, put a -0/-1 counter on Kjeldoran Home Guard and put a 0/1 white Deserter creature token onto the battlefield.
        Ability ability = new ConditionalTriggeredAbility(
                new EndOfCombatTriggeredAbility(new AddCountersSourceEffect(new BoostCounter(0, -1)), false),
                AttackedOrBlockedThisCombatSourceCondition.instance,
                "At end of combat, if {this} attacked or blocked this combat, put a -0/-1 counter on {this} and create a 0/1 white Deserter creature token.");
        ability.addEffect(new CreateTokenEffect(new DeserterToken()).setText("and create a 0/1 white Deserter creature token."));
        this.addAbility(ability, new AttackedOrBlockedThisCombatWatcher());
    }

    public KjeldoranHomeGuard(final KjeldoranHomeGuard card) {
        super(card);
    }

    @Override
    public KjeldoranHomeGuard copy() {
        return new KjeldoranHomeGuard(this);
    }
}
