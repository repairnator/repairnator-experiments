

package mage.filter.common;

import mage.constants.CardType;
import mage.filter.FilterPermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class FilterNonlandPermanent extends FilterPermanent {

    public FilterNonlandPermanent() {
        this("nonland permanent");
    }

    public FilterNonlandPermanent(String name) {
        super(name);
        this.add(Predicates.not(new CardTypePredicate(CardType.LAND)));
    }

    public FilterNonlandPermanent(final FilterNonlandPermanent filter) {
        super(filter);
    }

    @Override
    public FilterNonlandPermanent copy() {
        return new FilterNonlandPermanent(this);
    }

}
