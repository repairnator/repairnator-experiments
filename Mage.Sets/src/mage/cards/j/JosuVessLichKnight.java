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
package mage.cards.j;

import mage.MageInt;
import mage.abilities.common.EntersBattlefieldTriggeredAbility;
import mage.abilities.condition.common.KickedCondition;
import mage.abilities.decorator.ConditionalTriggeredAbility;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.keyword.KickerAbility;
import mage.abilities.keyword.MenaceAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.game.permanent.token.ZombieKnightToken;

import java.util.UUID;

public class JosuVessLichKnight extends CardImpl {

    public JosuVessLichKnight(UUID ownerID, CardSetInfo cardSetInfo){
        super(ownerID, cardSetInfo, new CardType[]{CardType.CREATURE}, "{2}{B}{B}");
        this.addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.ZOMBIE, SubType.KNIGHT);
        this.power = new MageInt(4);
        this.toughness = new MageInt(5);

        //Kicker {5}{B} (You may pay an additional {5}{B} as you cast this spell.)
        this.addAbility(new KickerAbility("{5}{B}"));

        //Menace
        this.addAbility(new MenaceAbility());

        //When Josu Vess, Lich Knight enters the battlefield, if it was kicked, create eight 2/2 black Zombie Knight creature tokens with menace.
        EntersBattlefieldTriggeredAbility ability = new EntersBattlefieldTriggeredAbility(new CreateTokenEffect(new ZombieKnightToken(), 8));
        this.addAbility(new ConditionalTriggeredAbility(ability, KickedCondition.instance,
                "When {this} enters the battlefield, if it was kicked, create eight 2/2 black Zombie Knight creature tokens with menace."));
    }

    public JosuVessLichKnight(final JosuVessLichKnight josuVessLichKnight){
        super(josuVessLichKnight);
    }

    @Override
    public Card copy() {
        return new JosuVessLichKnight(this);
    }
}
