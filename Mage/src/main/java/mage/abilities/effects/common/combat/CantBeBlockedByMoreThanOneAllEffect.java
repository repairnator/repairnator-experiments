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
package mage.abilities.effects.common.combat;

import mage.constants.Duration;
import mage.constants.Layer;
import mage.constants.Outcome;
import mage.constants.SubLayer;
import mage.abilities.Ability;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.filter.FilterPermanent;
import mage.game.Game;
import mage.game.permanent.Permanent;


import mage.util.CardUtil;

/**
 *
 * @author Quercitron
 */
public class CantBeBlockedByMoreThanOneAllEffect extends ContinuousEffectImpl {

    private FilterPermanent filter;
    protected int amount;

    public CantBeBlockedByMoreThanOneAllEffect(FilterPermanent filter) {
        this(1, filter, Duration.WhileOnBattlefield);
    }

    public CantBeBlockedByMoreThanOneAllEffect(int amount, FilterPermanent filter) {
        this(amount, filter, Duration.WhileOnBattlefield);
    }

    public CantBeBlockedByMoreThanOneAllEffect(int amount, FilterPermanent filter, Duration duration) {
        super(duration, Outcome.Benefit);
        this.amount = amount;
        this.filter = filter;
        staticText = new StringBuilder("Each ").append(filter.getMessage()).append(" can't be blocked by more than ")
                .append(CardUtil.numberToText(amount)).append(" creature").append(amount > 1 ? "s" : "").toString();
    }

    public CantBeBlockedByMoreThanOneAllEffect(final CantBeBlockedByMoreThanOneAllEffect effect) {
        super(effect);
        this.amount = effect.amount;
        this.filter = effect.filter;
    }

    @Override
    public CantBeBlockedByMoreThanOneAllEffect copy() {
        return new CantBeBlockedByMoreThanOneAllEffect(this);
    }

    @Override
    public boolean apply(Layer layer, SubLayer sublayer, Ability source, Game game) {
        switch (layer) {
            case RulesEffects:
                for (Permanent perm : game.getBattlefield().getActivePermanents(filter, source.getControllerId(), source.getSourceId(), game)) {
                    perm.setMaxBlockedBy(amount);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return false;
    }

    @Override
    public boolean hasLayer(Layer layer) {
        return layer == Layer.RulesEffects;
    }
}
