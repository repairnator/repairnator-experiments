
package mage.cards.n;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.common.AsEntersBattlefieldAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ContinuousRuleModifyingEffectImpl;
import mage.abilities.effects.common.NameACardEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public final class Nevermore extends CardImpl {

    public Nevermore(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{1}{W}{W}");

        // As Nevermore enters the battlefield, name a nonland card.
        this.addAbility(new AsEntersBattlefieldAbility(new NameACardEffect(NameACardEffect.TypeOfName.NON_LAND_NAME)));

        // The named card can't be cast.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new NevermoreEffect2()));

    }

    public Nevermore(final Nevermore card) {
        super(card);
    }

    @Override
    public Nevermore copy() {
        return new Nevermore(this);
    }

}

class NevermoreEffect2 extends ContinuousRuleModifyingEffectImpl {

    public NevermoreEffect2() {
        super(Duration.WhileOnBattlefield, Outcome.Detriment);
        staticText = "The named card can't be cast";
    }

    public NevermoreEffect2(final NevermoreEffect2 effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public NevermoreEffect2 copy() {
        return new NevermoreEffect2(this);
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (event.getType() == EventType.CAST_SPELL) {
            MageObject object = game.getObject(event.getSourceId());
            if (object != null && object.getName().equals(game.getState().getValue(source.getSourceId().toString() + NameACardEffect.INFO_KEY))) {
                return true;
            }
        }
        return false;
    }

}
