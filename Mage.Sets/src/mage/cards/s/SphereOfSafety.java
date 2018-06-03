
package mage.cards.s;

import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.mana.ManaCosts;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.combat.CantAttackYouUnlessPayManaAllEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Zone;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.events.GameEvent;

import java.util.UUID;

/**
 *
 * @author LevelX2
 */
public final class SphereOfSafety extends CardImpl {

    public SphereOfSafety(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{4}{W}");

        // Creatures can't attack you or a planeswalker you control unless their controller pays {X} for each of those creatures, where X is the number of enchantments you control.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new SphereOfSafetyPayManaToAttackAllEffect()));

    }

    public SphereOfSafety(final SphereOfSafety card) {
        super(card);
    }

    @Override
    public SphereOfSafety copy() {
        return new SphereOfSafety(this);
    }

}

class SphereOfSafetyPayManaToAttackAllEffect extends CantAttackYouUnlessPayManaAllEffect {

    SphereOfSafetyPayManaToAttackAllEffect() {
        super(null, true);
        staticText = "Creatures can't attack you or a planeswalker you control unless their controller pays {X} for each of those creatures, where X is the number of enchantments you control.";
    }

    SphereOfSafetyPayManaToAttackAllEffect(SphereOfSafetyPayManaToAttackAllEffect effect) {
        super(effect);
    }

    @Override
    public ManaCosts getManaCostToPay(GameEvent event, Ability source, Game game) {
        int enchantments = game.getBattlefield().countAll(StaticFilters.FILTER_ENCHANTMENT_PERMANENT, source.getControllerId(), game);
        if (enchantments > 0) {
            return new ManaCostsImpl<>("{" + enchantments + '}');
        }
        return null;
    }

    @Override
    public SphereOfSafetyPayManaToAttackAllEffect copy() {
        return new SphereOfSafetyPayManaToAttackAllEffect(this);
    }

}
