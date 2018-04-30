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
package mage.cards.t;

import java.util.UUID;
import mage.abilities.Mode;
import mage.abilities.effects.common.LookLibraryControllerEffect;
import mage.abilities.effects.common.continuous.BecomesChosenCreatureTypeTargetEffect;
import mage.abilities.effects.common.continuous.GainAbilityTargetEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author LoneFox
 */
public class TrickeryCharm extends CardImpl {

    public TrickeryCharm(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{U}");

        // Choose one - Target creature gains flying until end of turn
        this.getSpellAbility().addEffect(new GainAbilityTargetEffect(FlyingAbility.getInstance(), Duration.EndOfTurn));
        this.getSpellAbility().addTarget(new TargetCreaturePermanent());
        // or target creature becomes the creature type of your choice until end of turn
        Mode mode = new Mode();
        mode.getEffects().add(new BecomesChosenCreatureTypeTargetEffect());
        mode.getTargets().add(new TargetCreaturePermanent());
        this.getSpellAbility().addMode(mode);
        // or look at the top four cards of your library, then put them back in any order.
        mode = new Mode();
        mode.getEffects().add(new LookLibraryControllerEffect(4));
        this.getSpellAbility().addMode(mode);
    }

    public TrickeryCharm(final TrickeryCharm card) {
        super(card);
    }

    @Override
    public TrickeryCharm copy() {
        return new TrickeryCharm(this);
    }
}
