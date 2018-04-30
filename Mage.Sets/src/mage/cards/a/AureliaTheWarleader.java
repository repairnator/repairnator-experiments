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
package mage.cards.a;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.AdditionalCombatPhaseEffect;
import mage.abilities.effects.common.UntapAllControllerEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.abilities.keyword.HasteAbility;
import mage.abilities.keyword.VigilanceAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;

/**
*
* @author LevelX2
*/
public class AureliaTheWarleader extends CardImpl {

    public AureliaTheWarleader(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{2}{R}{R}{W}{W}");


        addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.ANGEL);
        this.power = new MageInt(3);
        this.toughness = new MageInt(4);

         // Flying, vigilance, haste
        this.addAbility(FlyingAbility.getInstance());
        this.addAbility(VigilanceAbility.getInstance());
        this.addAbility(HasteAbility.getInstance());

        // Whenever Aurelia, the Warleader attacks for the first time each turn, untap all creatures you control. After this phase, there is an additional combat phase.
        Ability ability = new AureliaAttacksTriggeredAbility(new UntapAllControllerEffect(new FilterControlledCreaturePermanent(),"untap all creatures you control"), false);
        ability.addEffect(new AdditionalCombatPhaseEffect());
        this.addAbility(ability);

    }

    public AureliaTheWarleader(final AureliaTheWarleader card) {
        super(card);
    }

    @Override
    public AureliaTheWarleader copy() {
        return new AureliaTheWarleader(this);
    }
}

class AureliaAttacksTriggeredAbility extends TriggeredAbilityImpl {

    protected String text;

    public AureliaAttacksTriggeredAbility(Effect effect, boolean optional) {
        super(Zone.BATTLEFIELD, effect, optional);
    }

    public AureliaAttacksTriggeredAbility(Effect effect, boolean optional, String text) {
        super(Zone.BATTLEFIELD, effect, optional);
        this.text = text;
    }

    public AureliaAttacksTriggeredAbility(final AureliaAttacksTriggeredAbility ability) {
        super(ability);
        this.text = ability.text;
    }

    @Override
    public void reset(Game game) {
        Card sourceCard = game.getCard(getSourceId());
        game.getState().setValue(getValueKey(sourceCard, game), 0);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.ATTACKER_DECLARED;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
       if (event.getSourceId().equals(this.getSourceId()) ) {
           Permanent sourceCard = game.getPermanent(getSourceId());
           Integer amountAttacks = (Integer) game.getState().getValue(getValueKey(sourceCard, game));
           if (amountAttacks == null || amountAttacks < 1) {
               if (amountAttacks == null) {
                   amountAttacks = 1;
               } else {
                   ++amountAttacks;
               }
               game.getState().setValue(getValueKey(sourceCard, game), amountAttacks);
               return true;
           }
       }
       return false;
    }

    protected String getValueKey(Card sourceCard, Game game) {
        if (sourceCard == null) {
            return "";
        }
        return new StringBuilder(this.getId().toString()).append(sourceCard.getZoneChangeCounter(game)).append("amountAttacks").toString();
    }

    @Override
    public String getRule() {
       if (text == null || text.isEmpty()) {

           return "Whenever {this} attacks for the first time each turn, " + super.getRule();
       }
       return text;
    }

    @Override
    public AureliaAttacksTriggeredAbility copy() {
       return new AureliaAttacksTriggeredAbility(this);
    }
}
