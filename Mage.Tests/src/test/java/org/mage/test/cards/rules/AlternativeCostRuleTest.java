package org.mage.test.cards.rules;

import mage.cards.Card;
import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Assert;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 *
 * @author magenoxx_at_googlemail.com
 */
public class AlternativeCostRuleTest extends CardTestPlayerBase {

    @Test
    public void testAlternativeCostDisplayed() {
        addCard(Zone.GRAVEYARD, playerA, "Firewild Borderpost");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        Card firewildBorderpost = playerA.getGraveyard().getCards(currentGame).iterator().next();
        boolean found = false;
        for (String rule : firewildBorderpost.getRules(currentGame)) {
            if (rule.startsWith("You may pay")) {
                found = true;
                break;
            }
        }
        Assert.assertTrue("Couldn't find rule text for alternative cost on a card: " + firewildBorderpost.getName(), found);
    }


}
