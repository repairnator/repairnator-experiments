

package mage.abilities.keyword;

import mage.constants.Zone;
import mage.abilities.ActivatedAbilityImpl;
import mage.abilities.costs.common.DiscardSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.Effect;
import mage.constants.TimingRule;



public class ChannelAbility extends ActivatedAbilityImpl {

    public ChannelAbility(String manaString, Effect effect) {
        this(manaString, effect, TimingRule.INSTANT);
    }
    
    public ChannelAbility(String manaString, Effect effect, TimingRule timing) {
        super(Zone.HAND, effect, new ManaCostsImpl(manaString));
        this.addCost(new DiscardSourceCost());
        this.timing = timing;
    }

    public ChannelAbility(final ChannelAbility ability) {
        super(ability);
    }

    @Override
    public ChannelAbility copy() {
        return new ChannelAbility(this);
    }

    @Override
    public String getRule() {
        StringBuilder sb = new StringBuilder("<i>Channel</i> &mdash; ");
        sb.append(super.getRule());
        if(this.timing == TimingRule.SORCERY) {
            sb.append(" Activate this ability only any time you could cast a sorcery.");
        }
        return sb.toString();
    }

}

