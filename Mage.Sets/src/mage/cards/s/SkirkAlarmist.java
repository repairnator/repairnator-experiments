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
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.DelayedTriggeredAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.common.delayed.AtTheBeginOfNextEndStepDelayedTriggeredAbility;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.SacrificeTargetEffect;
import mage.abilities.effects.common.TurnFaceUpTargetEffect;
import mage.constants.SubType;
import mage.abilities.keyword.HasteAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.TargetController;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.other.FaceDownPredicate;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.target.common.TargetCreaturePermanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author TheElk801
 */
public class SkirkAlarmist extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("face-down creature you control");

    static {
        filter.add(new FaceDownPredicate());
        filter.add(new ControllerPredicate(TargetController.YOU));
    }

    public SkirkAlarmist(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{R}");

        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.WIZARD);
        this.power = new MageInt(1);
        this.toughness = new MageInt(2);

        // Haste
        this.addAbility(HasteAbility.getInstance());

        // {tap}: Turn target face-down creature you control face up. At the beginning of the next end step, sacrifice it.
        Ability ability = new SimpleActivatedAbility(new TurnFaceUpTargetEffect(), new TapSourceCost());
        ability.addEffect(new SkirkAlarmistEffect());
        ability.addTarget(new TargetCreaturePermanent(filter));
        this.addAbility(ability);
    }

    public SkirkAlarmist(final SkirkAlarmist card) {
        super(card);
    }

    @Override
    public SkirkAlarmist copy() {
        return new SkirkAlarmist(this);
    }
}

class SkirkAlarmistEffect extends OneShotEffect {

    public SkirkAlarmistEffect() {
        super(Outcome.Sacrifice);
        staticText = "At the beginning of the next end step, sacrifice it";
    }

    public SkirkAlarmistEffect(final SkirkAlarmistEffect effect) {
        super(effect);
    }

    @Override
    public SkirkAlarmistEffect copy() {
        return new SkirkAlarmistEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = game.getPermanent(source.getFirstTarget());
        if (permanent != null) {
            SacrificeTargetEffect sacrificeEffect = new SacrificeTargetEffect("sacrifice this", source.getControllerId());
            sacrificeEffect.setTargetPointer(new FixedTarget(permanent, game));
            DelayedTriggeredAbility delayedAbility = new AtTheBeginOfNextEndStepDelayedTriggeredAbility(sacrificeEffect);
            game.addDelayedTriggeredAbility(delayedAbility, source);
            return true;
        }
        return false;
    }
}
