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
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.condition.common.SpellMasteryCondition;
import mage.abilities.decorator.ConditionalAsThoughEffect;
import mage.abilities.effects.AsThoughEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.effects.common.continuous.CastAsThoughItHadFlashSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.TappedPredicate;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author LevelX2
 */
public class SwiftReckoning extends CardImpl {

    private final static FilterCreaturePermanent filter = new FilterCreaturePermanent("tapped creature");

    static {
        filter.add(new TappedPredicate());
    }

    public SwiftReckoning(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{1}{W}");

        // <i>Spell mastery</i> — If there are two or more instant and/or sorcery cards in your graveyard, you may cast Swift Reckoning as though it had flash.
        AsThoughEffect effect = new CastAsThoughItHadFlashSourceEffect(Duration.EndOfGame);
        effect.setText("<i>Spell mastery</i> &mdash; If there are two or more instant and/or sorcery cards in your graveyard, you may cast {this} as though it had flash");
        this.addAbility(new SimpleStaticAbility(Zone.ALL, new ConditionalAsThoughEffect(effect,
                SpellMasteryCondition.instance)));
        // Destroy target tapped creature.
        this.getSpellAbility().addEffect(new DestroyTargetEffect());
        this.getSpellAbility().addTarget(new TargetCreaturePermanent(filter));
    }

    public SwiftReckoning(final SwiftReckoning card) {
        super(card);
    }

    @Override
    public SwiftReckoning copy() {
        return new SwiftReckoning(this);
    }
}
