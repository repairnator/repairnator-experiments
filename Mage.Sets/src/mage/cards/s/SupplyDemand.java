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
package mage.cards.s;

import java.util.UUID;
import mage.abilities.dynamicvalue.common.ManacostVariableValue;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.abilities.effects.common.search.SearchLibraryPutInHandEffect;
import mage.cards.CardSetInfo;
import mage.cards.SplitCard;
import mage.constants.CardType;
import mage.constants.SpellAbilityType;
import mage.filter.FilterCard;
import mage.filter.predicate.mageobject.MulticoloredPredicate;
import mage.game.permanent.token.SaprolingToken;
import mage.target.common.TargetCardInLibrary;

/**
 *
 * @author LevelX2
 */
public class SupplyDemand extends SplitCard {

    private static final FilterCard filter = new FilterCard("multicolored card");

    static {
        filter.add(new MulticoloredPredicate());
    }

    public SupplyDemand(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{X}{G}{W}", "{1}{W}{U}", SpellAbilityType.SPLIT);

        // Supply
        // create X 1/1 green Saproling creature tokens.
        getLeftHalfCard().getSpellAbility().addEffect(new CreateTokenEffect(new SaprolingToken(), new ManacostVariableValue()));

        // Demand
        // Search your library for a multicolored card, reveal it, and put it into your hand. Then shuffle your library.
        getRightHalfCard().getSpellAbility().addEffect(new SearchLibraryPutInHandEffect(new TargetCardInLibrary(1, filter), true));

    }

    public SupplyDemand(final SupplyDemand card) {
        super(card);
    }

    @Override
    public SupplyDemand copy() {
        return new SupplyDemand(this);
    }
}
