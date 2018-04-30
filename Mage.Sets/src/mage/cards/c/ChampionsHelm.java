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
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.condition.common.EquippedHasSupertypeCondition;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.decorator.ConditionalContinuousEffect;
import mage.abilities.effects.common.continuous.BoostEquippedEffect;
import mage.abilities.effects.common.continuous.GainAbilityAttachedEffect;
import mage.abilities.keyword.EquipAbility;
import mage.abilities.keyword.HexproofAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;

/**
 *
 * @author LevelX2
 */
public class ChampionsHelm extends CardImpl {

    private static final String staticText = "As long as equipped creature is legendary, it has hexproof";
    
    public ChampionsHelm(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT},"{3}");
        this.subtype.add(SubType.EQUIPMENT);

        // Equipped creature gets +2/+2.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new BoostEquippedEffect(2, 2)));
        
        // As long as equipped creature is legendary, it has hexproof.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new ConditionalContinuousEffect(
                new GainAbilityAttachedEffect(HexproofAbility.getInstance(), AttachmentType.EQUIPMENT), 
                new EquippedHasSupertypeCondition(SuperType.LEGENDARY), staticText)));
        
        // Equip {1}
        this.addAbility(new EquipAbility(Outcome.AddAbility, new GenericManaCost(1)));
    }

    public ChampionsHelm(final ChampionsHelm card) {
        super(card);
    }

    @Override
    public ChampionsHelm copy() {
        return new ChampionsHelm(this);
    }
}
