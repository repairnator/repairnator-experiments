
package mage.cards.v;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.PutIntoGraveFromAnywhereSourceTriggeredAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ReplacementEffectImpl;
import mage.abilities.effects.common.ShuffleIntoLibrarySourceEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;

/**
 *
 * @author emerald000
 */
public final class Vigor extends CardImpl {

    public Vigor(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{3}{G}{G}{G}");
        this.subtype.add(SubType.ELEMENTAL);
        this.subtype.add(SubType.INCARNATION);

        this.power = new MageInt(6);
        this.toughness = new MageInt(6);

        // Trample
        this.addAbility(TrampleAbility.getInstance());
        
        // If damage would be dealt to a creature you control other than Vigor, prevent that damage. Put a +1/+1 counter on that creature for each 1 damage prevented this way.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new VigorReplacementEffect()));
        
        // When Vigor is put into a graveyard from anywhere, shuffle it into its owner's library.
        this.addAbility(new PutIntoGraveFromAnywhereSourceTriggeredAbility(new ShuffleIntoLibrarySourceEffect()));
    }

    public Vigor(final Vigor card) {
        super(card);
    }

    @Override
    public Vigor copy() {
        return new Vigor(this);
    }
}

class VigorReplacementEffect extends ReplacementEffectImpl {

    VigorReplacementEffect() {
        super(Duration.WhileOnBattlefield, Outcome.BoostCreature);
        staticText = "If damage would be dealt to a creature you control other than {this}, prevent that damage. Put a +1/+1 counter on that creature for each 1 damage prevented this way";
    }

    VigorReplacementEffect(final VigorReplacementEffect effect) {
        super(effect);
    }

    @Override
    public boolean replaceEvent(GameEvent event, Ability source, Game game) {
        GameEvent preventEvent = new GameEvent(GameEvent.EventType.PREVENT_DAMAGE, source.getFirstTarget(), source.getSourceId(), source.getControllerId(), event.getAmount(), false);
        if (!game.replaceEvent(preventEvent)) {
            int preventedDamage = event.getAmount();
            event.setAmount(0);
            Permanent permanent = game.getPermanent(event.getTargetId());
            if (permanent != null) {
                permanent.addCounters(CounterType.P1P1.createInstance(preventedDamage), source, game);
            }
            game.fireEvent(GameEvent.getEvent(GameEvent.EventType.PREVENTED_DAMAGE, source.getFirstTarget(), source.getSourceId(), source.getControllerId(), preventedDamage));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checksEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.DAMAGE_CREATURE;
    }
    
    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        return event.getPlayerId().equals(source.getControllerId()) 
                && !event.getTargetId().equals(source.getSourceId());
    }

    @Override
    public VigorReplacementEffect copy() {
        return new VigorReplacementEffect(this);
    }
}