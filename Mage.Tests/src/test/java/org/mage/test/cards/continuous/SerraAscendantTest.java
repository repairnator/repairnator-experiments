
package org.mage.test.cards.continuous;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 *
 * @author LevelX2
 */

public class SerraAscendantTest extends CardTestPlayerBase {

    /**
     * The game goes on; he plays his Serra Ascendant on turn one, passes the
     * turn, you play your newly unbanned Wild Nacatl with a Stomping Ground and
     * also pass the turn. On turn 2, he casts a Martyr of Sands and sacrifices
     * it, revealing 3 white cards to gain 9 life and end up at 29. He goes to
     * the combat phase, declares Serra as an attacker, and you happily block
     * him, thinking that this is such a bad move from him. After the damage is
     * dealt, the Serra is still there, bigger than ever.
     */
    @Test
    public void testSilence() {
        addCard(Zone.HAND, playerA, "Plains", 2);
        // As long as you have 30 or more life, Serra Ascendant gets +5/+5 and has flying.
        addCard(Zone.HAND, playerA, "Serra Ascendant");
        // {1}, Reveal X white cards from your hand, Sacrifice Martyr of Sands: You gain three times X life.
        addCard(Zone.HAND, playerA, "Martyr of Sands");
        addCard(Zone.HAND, playerA, "Silvercoat Lion",3);

        addCard(Zone.HAND, playerB, "Stomping Ground", 1);
        addCard(Zone.HAND, playerB, "Wild Nacatl", 1);
        
        playLand(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Plains");
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Serra Ascendant");
        
        playLand(2, PhaseStep.PRECOMBAT_MAIN, playerB, "Stomping Ground");
        setChoice(playerB, "Yes");
        castSpell(2, PhaseStep.PRECOMBAT_MAIN, playerB, "Wild Nacatl");
        
        playLand(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Plains");
        castSpell(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Martyr of Sands");
        activateAbility(3, PhaseStep.PRECOMBAT_MAIN, playerA, "{1}, You may reveal X white cards from your hand");
        setChoice(playerA,"X=3");
        
        attack(3, playerA, "Serra Ascendant");
        block(3, playerB, "Wild Nacatl", "Serra Ascendant");

        setStopAt(3, PhaseStep.POSTCOMBAT_MAIN);
        
        execute();

        assertGraveyardCount(playerA, "Martyr of Sands", 1);

        assertLife(playerB, 18);
        assertLife(playerA, 30);
        
        assertPermanentCount(playerB, "Wild Nacatl", 1);
    
        assertPermanentCount(playerA, "Serra Ascendant", 1);
        assertPowerToughness(playerA, "Serra Ascendant", 6, 6);
        
    }       

}