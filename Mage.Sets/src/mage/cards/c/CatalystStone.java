
package mage.cards.c;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.SpellAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.common.cost.CostModificationEffectImpl;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.choices.ChoiceImpl;
import mage.constants.CardType;
import mage.constants.CostModificationType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.SpellAbilityCastMode;
import mage.constants.Zone;
import static mage.filter.predicate.permanent.ControllerControlsIslandPredicate.filter;
import mage.game.Game;
import mage.players.Player;
import mage.util.CardUtil;

/**
 *
 * @author LevelX2
 */
public final class CatalystStone extends CardImpl {

    public CatalystStone(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT}, "{2}");

        // Flashback costs you pay cost up to {2} less.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new CatalystStoneCostReductionEffect()));

        // Flashback costs your opponents pay cost {2} more.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new CatalystStoneCostRaiseEffect()));

    }

    public CatalystStone(final CatalystStone card) {
        super(card);
    }

    @Override
    public CatalystStone copy() {
        return new CatalystStone(this);
    }
}

class CatalystStoneCostReductionEffect extends CostModificationEffectImpl {

    public CatalystStoneCostReductionEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Benefit, CostModificationType.REDUCE_COST);
        this.staticText = "Flashback costs you pay cost up to {2} less";
    }

    protected CatalystStoneCostReductionEffect(final CatalystStoneCostReductionEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source, Ability abilityToModify) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            int generic = abilityToModify.getManaCostsToPay().getMana().getGeneric();
            if (generic > 0) {
                ChoiceImpl choice = new ChoiceImpl(false);
                Set<String> set = new LinkedHashSet<>();
                for (int i = 0; i <= Math.min(2, generic); i++) {
                    set.add(String.valueOf(i));
                }
                choice.setChoices(set);
                MageObject mageObject = game.getObject(abilityToModify.getSourceId());
                choice.setMessage("Reduce cost of " + (mageObject != null ? mageObject.getIdName() : filter.getMessage()));
                if (controller.choose(Outcome.Benefit, choice, game)) {
                    generic = Integer.parseInt(choice.getChoice());
                }
                CardUtil.reduceCost(abilityToModify, generic);
            }
            return true;
        }
        return false;

    }

    @Override
    public boolean applies(Ability abilityToModify, Ability source, Game game) {
        if (abilityToModify instanceof SpellAbility) {
            if (abilityToModify.getControllerId().equals(source.getControllerId())) {
                return SpellAbilityCastMode.FLASHBACK.equals(((SpellAbility) abilityToModify).getSpellAbilityCastMode());
            }
        }
        return false;
    }

    @Override
    public CatalystStoneCostReductionEffect copy() {
        return new CatalystStoneCostReductionEffect(this);
    }
}

class CatalystStoneCostRaiseEffect extends CostModificationEffectImpl {

    public CatalystStoneCostRaiseEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Benefit, CostModificationType.INCREASE_COST);
        this.staticText = "Flashback costs your opponents pay cost {2} more";
    }

    protected CatalystStoneCostRaiseEffect(final CatalystStoneCostRaiseEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source, Ability abilityToModify) {
        CardUtil.increaseCost(abilityToModify, 2);
        return true;
    }

    @Override
    public boolean applies(Ability abilityToModify, Ability source, Game game) {
        if (abilityToModify instanceof SpellAbility) {
            if (game.getOpponents(source.getControllerId()).contains(abilityToModify.getControllerId())) {
                return SpellAbilityCastMode.FLASHBACK.equals(((SpellAbility) abilityToModify).getSpellAbilityCastMode());
            }
        }
        return false;
    }

    @Override
    public CatalystStoneCostRaiseEffect copy() {
        return new CatalystStoneCostRaiseEffect(this);
    }
}
