
package mage.cards.d;

import java.util.UUID;
import mage.abilities.effects.common.DestroyAllEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import static mage.filter.StaticFilters.FILTER_PERMANENT_CREATURES;

/**
 *
 * @author Loki
 */
public final class Damnation extends CardImpl {

    public Damnation(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{2}{B}{B}");

        // Destroy all creatures. They can't be regenerated.
        this.getSpellAbility().addEffect(new DestroyAllEffect(FILTER_PERMANENT_CREATURES, true));
    }

    public Damnation(final Damnation card) {
        super(card);
    }

    @Override
    public Damnation copy() {
        return new Damnation(this);
    }
}
