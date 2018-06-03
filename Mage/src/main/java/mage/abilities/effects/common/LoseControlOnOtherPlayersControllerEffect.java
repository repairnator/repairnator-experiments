

package mage.abilities.effects.common;

import mage.constants.Outcome;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.game.Game;
import mage.players.Player;

/**
 *
 * @author nantuko
 */
public class LoseControlOnOtherPlayersControllerEffect extends OneShotEffect {

    public LoseControlOnOtherPlayersControllerEffect(String controllingPlayerName, String controlledPlayerName) {
        super(Outcome.Detriment);
        staticText = controllingPlayerName + " lost control over " + controlledPlayerName;
    }

    public LoseControlOnOtherPlayersControllerEffect(final LoseControlOnOtherPlayersControllerEffect effect) {
        super(effect);
    }

    @Override
    public LoseControlOnOtherPlayersControllerEffect copy() {
        return new LoseControlOnOtherPlayersControllerEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getControllerId());
        if (player != null) {
            player.resetOtherTurnsControlled();
            Player targetPlayer = game.getPlayer(this.getTargetPointer().getFirst(game, source));
            if (targetPlayer != null) {
                targetPlayer.setGameUnderYourControl(true);
            }
            return true;
        }
        return false;
    }

}
