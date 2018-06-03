

package mage.cards.p;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.PreventionEffectImpl;
import mage.abilities.keyword.InfectAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public final class PhyrexianHydra extends CardImpl {

    public PhyrexianHydra(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{3}{G}{G}");
        this.subtype.add(SubType.HYDRA);

        this.power = new MageInt(7);
        this.toughness = new MageInt(7);

        this.addAbility(InfectAbility.getInstance());
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new PhyrexianHydraEffect()));
    }

    public PhyrexianHydra(final PhyrexianHydra card) {
        super(card);
    }

    @Override
    public PhyrexianHydra copy() {
        return new PhyrexianHydra(this);
    }

}

class PhyrexianHydraEffect extends PreventionEffectImpl {

    public PhyrexianHydraEffect() {
        super(Duration.WhileOnBattlefield);
    }

    public PhyrexianHydraEffect(final PhyrexianHydraEffect effect) {
        super(effect);
        staticText = "If damage would be dealt to {this}, prevent that damage. Put a -1/-1 counter on {this} for each 1 damage prevented this way";
    }

    @Override
    public PhyrexianHydraEffect copy() {
        return new PhyrexianHydraEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        boolean retValue = false;
        GameEvent preventEvent = new GameEvent(GameEvent.EventType.PREVENT_DAMAGE, source.getFirstTarget(), source.getSourceId(), source.getControllerId(), event.getAmount(), false);
        int damage = event.getAmount();
        if (!game.replaceEvent(preventEvent)) {
            event.setAmount(0);
            game.fireEvent(GameEvent.getEvent(GameEvent.EventType.PREVENTED_DAMAGE, source.getFirstTarget(), source.getSourceId(), source.getControllerId(), damage));
            retValue = true;
        }
        Permanent permanent = game.getPermanent(source.getSourceId());
        if (permanent != null) {
            permanent.addCounters(CounterType.M1M1.createInstance(damage), source, game);
        }
        return retValue;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (super.applies(event, source, game)) {
            if (event.getTargetId().equals(source.getSourceId())) {
                return true;
            }
        }
        return false;
    }

}
