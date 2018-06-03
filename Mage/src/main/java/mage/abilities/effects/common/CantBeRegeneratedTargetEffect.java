
package mage.abilities.effects.common;

import mage.abilities.Ability;
import mage.abilities.Mode;
import mage.abilities.effects.ContinuousRuleModifyingEffectImpl;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;

/**
 *
 * @author LevelX2
 */
public class CantBeRegeneratedTargetEffect extends ContinuousRuleModifyingEffectImpl {

    public CantBeRegeneratedTargetEffect(Duration duration) {
        super(duration, Outcome.Benefit, false, false);
    }

    public CantBeRegeneratedTargetEffect(final CantBeRegeneratedTargetEffect effect) {
        super(effect);
    }

    @Override
    public CantBeRegeneratedTargetEffect copy() {
        return new CantBeRegeneratedTargetEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == EventType.REGENERATE;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        return getTargetPointer().getTargets(game, source).contains(event.getTargetId());
    }

    @Override
    public String getText(Mode mode) {
        if (staticText != null && !staticText.isEmpty()) {
            return staticText;
        }
        StringBuilder sb = new StringBuilder();
        if (!mode.getTargets().isEmpty()) {
            sb.append("target ").append(mode.getTargets().get(0).getTargetName());
        }
        sb.append(" can't be regenerated");
        if (!duration.toString().isEmpty()) {
            if (duration == Duration.EndOfTurn) {
                sb.append(" this turn");
            } else {
                sb.append(' ').append(duration.toString());
            }
        }
        return sb.toString();
    }

}
