
package mage.cards.s;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.AttachEffect;
import mage.abilities.effects.common.continuous.BecomesCreatureIfVehicleEffect;
import mage.abilities.effects.common.continuous.BoostEnchantedEffect;
import mage.abilities.effects.common.continuous.GainAbilityAttachedEffect;
import mage.abilities.keyword.EnchantAbility;
import mage.abilities.keyword.FirstStrikeAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.FilterPermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.CardTypePredicate;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.target.TargetPermanent;

/**
 * @author JRHerlehy
 */
public final class SiegeModification extends CardImpl {

    private static final FilterPermanent filter = new FilterPermanent("creature or vehicle");

    static {
        filter.add(Predicates.or(new CardTypePredicate(CardType.CREATURE),
                new SubtypePredicate(SubType.VEHICLE)));
    }

    public SiegeModification(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{1}{R}{R}");

        this.subtype.add(SubType.AURA);

        // Enchant creature or Vehicle
        TargetPermanent auraTarget = new TargetPermanent(filter);
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.Benefit));
        Ability ability = new EnchantAbility(auraTarget.getTargetName());
        this.addAbility(ability);

        // As long as enchanted permanent is a Vehicle, it's a creature in addition to its other types.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new BecomesCreatureIfVehicleEffect()));

        // Enchanted creature gets +3/+0 and has first strike.
        Effect effect = new BoostEnchantedEffect(3, 0);
        effect.setText("Enchanted creature gets +3/+0");
        ability = new SimpleStaticAbility(Zone.BATTLEFIELD, effect);
        effect = new GainAbilityAttachedEffect(FirstStrikeAbility.getInstance(), AttachmentType.AURA);
        effect.setText(" and has first strike");
        ability.addEffect(effect);
        this.addAbility(ability);
    }

    public SiegeModification(final SiegeModification card) {
        super(card);
    }

    @Override
    public SiegeModification copy() {
        return new SiegeModification(this);
    }


}
