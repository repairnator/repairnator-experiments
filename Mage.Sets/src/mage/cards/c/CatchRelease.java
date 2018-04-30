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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.UntapTargetEffect;
import mage.abilities.effects.common.continuous.GainAbilityTargetEffect;
import mage.abilities.effects.common.continuous.GainControlTargetEffect;
import mage.abilities.keyword.HasteAbility;
import mage.cards.CardSetInfo;
import mage.cards.SplitCard;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.SpellAbilityType;
import mage.filter.FilterPermanent;
import mage.filter.common.*;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.Target;
import mage.target.TargetPermanent;
import mage.target.common.TargetControlledPermanent;

public class CatchRelease extends SplitCard {

    public CatchRelease(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{1}{U}{R}", "{4}{R}{W}", SpellAbilityType.SPLIT_FUSED);

        // Catch
        // Gain control of target permanent until end of turn. Untap it. It gains haste until end of turn.
        getLeftHalfCard().getSpellAbility().addTarget(new TargetPermanent(new FilterPermanent()));
        getLeftHalfCard().getSpellAbility().addEffect(new GainControlTargetEffect(Duration.EndOfTurn));
        Effect effect = new UntapTargetEffect();
        effect.setText("Untap it");
        this.getSpellAbility().addEffect(effect);
        getLeftHalfCard().getSpellAbility().addEffect(effect);
        effect = new GainAbilityTargetEffect(HasteAbility.getInstance(), Duration.EndOfTurn);
        effect.setText("It gains haste until end of turn");
        getLeftHalfCard().getSpellAbility().addEffect(effect);

        // Release
        // Each player sacrifices an artifact, a creature, an enchantment, a land, and a planeswalker.
        getRightHalfCard().getSpellAbility().addEffect(new ReleaseSacrificeEffect());

    }

    public CatchRelease(final CatchRelease card) {
        super(card);
    }

    @Override
    public CatchRelease copy() {
        return new CatchRelease(this);
    }
}

class ReleaseSacrificeEffect extends OneShotEffect {

    public ReleaseSacrificeEffect() {
        super(Outcome.DestroyPermanent);
        staticText = "Each player sacrifices an artifact, a creature, an enchantment, a land, and a planeswalker";
    }

    public ReleaseSacrificeEffect(ReleaseSacrificeEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        List<UUID> chosen = new ArrayList<>();
        Player controller = game.getPlayer(source.getControllerId());
        if (controller == null) {
            return false;
        }
        for (UUID playerId : game.getState().getPlayersInRange(controller.getId(), game)) {
            Player player = game.getPlayer(playerId);

            Target target1 = new TargetControlledPermanent(1, 1, new FilterControlledArtifactPermanent(), true);
            Target target2 = new TargetControlledPermanent(1, 1, new FilterControlledCreaturePermanent(), true);
            Target target3 = new TargetControlledPermanent(1, 1, new FilterControlledEnchantmentPermanent(), true);
            Target target4 = new TargetControlledPermanent(1, 1, new FilterControlledLandPermanent(), true);
            Target target5 = new TargetControlledPermanent(1, 1, new FilterControlledPlaneswalkerPermanent(), true);

            if (target1.canChoose(player.getId(), game)) {
                while (player.canRespond() && !target1.isChosen() && target1.canChoose(player.getId(), game)) {
                    player.chooseTarget(Outcome.Benefit, target1, source, game);
                }
                Permanent artifact = game.getPermanent(target1.getFirstTarget());
                if (artifact != null) {
                    chosen.add(artifact.getId());
                }
                target1.clearChosen();
            }

            if (target2.canChoose(player.getId(), game)) {
                while (player.canRespond() && !target2.isChosen() && target2.canChoose(player.getId(), game)) {
                    player.chooseTarget(Outcome.Benefit, target2, source, game);
                }
                Permanent creature = game.getPermanent(target2.getFirstTarget());
                if (creature != null) {
                    chosen.add(creature.getId());
                }
                target2.clearChosen();
            }

            if (target3.canChoose(player.getId(), game)) {
                while (player.canRespond() && !target3.isChosen() && target3.canChoose(player.getId(), game)) {
                    player.chooseTarget(Outcome.Benefit, target3, source, game);
                }
                Permanent enchantment = game.getPermanent(target3.getFirstTarget());
                if (enchantment != null) {
                    chosen.add(enchantment.getId());
                }
                target3.clearChosen();
            }

            if (target4.canChoose(player.getId(), game)) {
                while (player.canRespond() && !target4.isChosen() && target4.canChoose(player.getId(), game)) {
                    player.chooseTarget(Outcome.Benefit, target4, source, game);
                }
                Permanent land = game.getPermanent(target4.getFirstTarget());
                if (land != null) {
                    chosen.add(land.getId());
                }
                target4.clearChosen();
            }

            if (target5.canChoose(player.getId(), game)) {
                while (player.canRespond() && !target5.isChosen() && target5.canChoose(player.getId(), game)) {
                    player.chooseTarget(Outcome.Benefit, target5, source, game);
                }
                Permanent planeswalker = game.getPermanent(target5.getFirstTarget());
                if (planeswalker != null) {
                    chosen.add(planeswalker.getId());
                }
                target5.clearChosen();
            }

        }

        for (UUID uuid : chosen) {
            Permanent permanent = game.getPermanent(uuid);
            if (permanent != null) {
                permanent.sacrifice(source.getSourceId(), game);
            }
        }
        return true;
    }

    @Override
    public ReleaseSacrificeEffect copy() {
        return new ReleaseSacrificeEffect(this);
    }
}
