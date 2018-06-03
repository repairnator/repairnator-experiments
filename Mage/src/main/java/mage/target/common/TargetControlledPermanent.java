

package mage.target.common;

import mage.filter.common.FilterControlledPermanent;
import mage.target.TargetPermanent;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class TargetControlledPermanent extends TargetPermanent {

    public TargetControlledPermanent() {
        this(1, 1, new FilterControlledPermanent(), false);
    }

    public TargetControlledPermanent(int numTargets) {
        this(numTargets, numTargets, new FilterControlledPermanent(), false);
    }

    public TargetControlledPermanent(FilterControlledPermanent filter) {
        this(1, 1, filter, false);
    }

    public TargetControlledPermanent(int minNumTargets, int maxNumTargets, FilterControlledPermanent filter, boolean notTarget) {
        super(minNumTargets, maxNumTargets, filter, notTarget);
    }

    public TargetControlledPermanent(final TargetControlledPermanent target) {
        super(target);
    }

    @Override
    public TargetControlledPermanent copy() {
        return new TargetControlledPermanent(this);
    }
}
