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
package mage.cards.d;

import java.util.UUID;
import mage.ObjectColor;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.effects.common.LoseLifeTargetControllerEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.ColorPredicate;
import mage.target.common.TargetControlledCreaturePermanent;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author LoneFox
 */
public class DeathBomb extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("nonblack creature");
    static {
        filter.add(Predicates.not(new ColorPredicate(ObjectColor.BLACK)));
    }

    public DeathBomb(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{3}{B}");

        // As an additional cost to cast Death Bomb, sacrifice a creature.
        this.getSpellAbility().addCost(new SacrificeTargetCost(new TargetControlledCreaturePermanent(1,1, new FilterControlledCreaturePermanent("a creature"), true)));
        // Destroy target nonblack creature. It can't be regenerated. Its controller loses 2 life.
        this.getSpellAbility().addEffect(new DestroyTargetEffect(true));
        this.getSpellAbility().addEffect(new LoseLifeTargetControllerEffect(2));
        this.getSpellAbility().addTarget(new TargetCreaturePermanent(filter));
    }

    public DeathBomb(final DeathBomb card) {
        super(card);
    }

    @Override
    public DeathBomb copy() {
        return new DeathBomb(this);
    }
}
