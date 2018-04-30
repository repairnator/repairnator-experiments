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
package mage.cards.i;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.CastOnlyDuringPhaseStepSourceAbility;
import mage.abilities.condition.common.OnOpponentsTurnCondition;
import mage.abilities.effects.ContinuousEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.RequirementEffect;
import mage.abilities.effects.RestrictionEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.PhaseStep;
import mage.constants.TurnPhase;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.turn.Phase;
import mage.game.turn.TurnMod;

/**
 *
 * @author LevelX2
 */
public class IllusionistsGambit extends CardImpl {

    public IllusionistsGambit(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{2}{U}{U}");

        // Cast Illusionist's Gambit only during the declare blockers step on an opponent's turn.
        this.addAbility(new CastOnlyDuringPhaseStepSourceAbility(PhaseStep.DECLARE_BLOCKERS, OnOpponentsTurnCondition.instance));

        // Remove all attacking creatures from combat and untap them. After this phase, there is an additional combat phase. Each of those creatures attacks that combat if able. They can't attack you or a planeswalker you control that combat.
        this.getSpellAbility().addEffect(new IllusionistsGambitRemoveFromCombatEffect());
    }

    public IllusionistsGambit(final IllusionistsGambit card) {
        super(card);
    }

    @Override
    public IllusionistsGambit copy() {
        return new IllusionistsGambit(this);
    }
}

class IllusionistsGambitRemoveFromCombatEffect extends OneShotEffect {

    public IllusionistsGambitRemoveFromCombatEffect() {
        super(Outcome.Benefit);
        this.staticText = "Remove all attacking creatures from combat and untap them. After this phase, there is an additional combat phase. Each of those creatures attacks that combat if able. They can't attack you or a planeswalker you control that combat";
    }

    public IllusionistsGambitRemoveFromCombatEffect(final IllusionistsGambitRemoveFromCombatEffect effect) {
        super(effect);
    }

    @Override
    public IllusionistsGambitRemoveFromCombatEffect copy() {
        return new IllusionistsGambitRemoveFromCombatEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        List<UUID> attackers = game.getCombat().getAttackers();
        for (UUID attackerId : attackers) {
            Permanent creature = game.getPermanent(attackerId);
            if (creature != null) {
                creature.removeFromCombat(game);
                creature.untap(game);
            }
        }
        if (!attackers.isEmpty()) {
            Phase phase = game.getTurn().getPhase();
            game.getState().getTurnMods().add(new TurnMod(game.getActivePlayerId(), TurnPhase.COMBAT, null, false));
            ContinuousEffect effect = new IllusionistsGambitRequirementEffect(attackers, phase);
            game.addEffect(effect, source);
            effect = new IllusionistsGambitRestrictionEffect(attackers, phase);
            game.addEffect(effect, source);

        }
        return true;
    }
}

class IllusionistsGambitRequirementEffect extends RequirementEffect {

    private List attackers;
    private Phase phase;

    public IllusionistsGambitRequirementEffect(List attackers, Phase phase) {
        super(Duration.Custom);
        this.attackers = attackers;
        this.phase = phase;
        this.staticText = "Each of those creatures attacks that combat if able";
    }

    public IllusionistsGambitRequirementEffect(final IllusionistsGambitRequirementEffect effect) {
        super(effect);
        this.attackers = effect.attackers;
        this.phase = effect.phase;
    }

    @Override
    public IllusionistsGambitRequirementEffect copy() {
        return new IllusionistsGambitRequirementEffect(this);
    }

    @Override
    public boolean applies(Permanent permanent, Ability source, Game game) {
        if (attackers.contains(permanent.getId())) {
            return game.getOpponents(permanent.getControllerId()).size() > 1;
        }
        return false;
    }

    @Override
    public boolean isInactive(Ability source, Game game) {
        if (game.getTurn().getStepType() == PhaseStep.END_COMBAT) {
            if (!Objects.equals(game.getTurn().getPhase(), phase)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mustAttack(Game game) {
        return true;
    }

    @Override
    public boolean mustBlock(Game game) {
        return false;
    }
}

class IllusionistsGambitRestrictionEffect extends RestrictionEffect {

    private final List attackers;
    private final Phase phase;

    public IllusionistsGambitRestrictionEffect(List attackers, Phase phase) {
        super(Duration.Custom, Outcome.Benefit);
        this.attackers = attackers;
        this.phase = phase;
        staticText = "They can't attack you or a planeswalker you control that combat";
    }

    public IllusionistsGambitRestrictionEffect(final IllusionistsGambitRestrictionEffect effect) {
        super(effect);
        this.attackers = effect.attackers;
        this.phase = effect.phase;
    }

    @Override
    public boolean applies(Permanent permanent, Ability source, Game game) {
        return attackers.contains(permanent.getId());
    }

    @Override
    public boolean isInactive(Ability source, Game game) {
        if (game.getTurn().getStepType() == PhaseStep.END_COMBAT) {
            if (!Objects.equals(game.getTurn().getPhase(), phase)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canAttack(Permanent attacker, UUID defenderId, Ability source, Game game) {
        if (defenderId.equals(source.getControllerId())) {
            return false;
        }
        // planeswalker
        Permanent permanent = game.getPermanent(defenderId);
        if (permanent != null && permanent.getControllerId().equals(source.getControllerId())
                && permanent.isPlaneswalker()) {
            return false;
        }
        return true;
    }

    @Override
    public IllusionistsGambitRestrictionEffect copy() {
        return new IllusionistsGambitRestrictionEffect(this);
    }

}
