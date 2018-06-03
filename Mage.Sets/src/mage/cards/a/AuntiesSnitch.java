
package mage.cards.a;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.CantBlockAbility;
import mage.abilities.effects.common.ReturnToHandSourceEffect;
import mage.abilities.keyword.ProwlAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.TargetController;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.game.Game;
import mage.game.events.DamagedPlayerEvent;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;

/**
 *
 * @author LevelX2
 */
public final class AuntiesSnitch extends CardImpl {

    public AuntiesSnitch(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{2}{B}");
        this.subtype.add(SubType.GOBLIN);
        this.subtype.add(SubType.ROGUE);

        this.power = new MageInt(3);
        this.toughness = new MageInt(1);

        // Auntie's Snitch can't block.
        this.addAbility(new CantBlockAbility());
        // Prowl {1}{B}
        this.addAbility(new ProwlAbility(this, "{1}{B}"));
        // Whenever a Goblin or Rogue you control deals combat damage to a player, if Auntie's Snitch is in your graveyard, you may return Auntie's Snitch to your hand.
        this.addAbility(new AuntiesSnitchTriggeredAbility());
    }

    public AuntiesSnitch(final AuntiesSnitch card) {
        super(card);
    }

    @Override
    public AuntiesSnitch copy() {
        return new AuntiesSnitch(this);
    }
}

class AuntiesSnitchTriggeredAbility extends TriggeredAbilityImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("Goblin or Rogue you control");
    static {
        filter.add(new ControllerPredicate(TargetController.YOU));
        filter.add(Predicates.or(new SubtypePredicate(SubType.GOBLIN), new SubtypePredicate(SubType.ROGUE)));
    }

    public AuntiesSnitchTriggeredAbility() {
        super(Zone.GRAVEYARD, new ReturnToHandSourceEffect(), true);
    }

    public AuntiesSnitchTriggeredAbility(final AuntiesSnitchTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public AuntiesSnitchTriggeredAbility copy() {
        return new AuntiesSnitchTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_PLAYER;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        DamagedPlayerEvent damageEvent = (DamagedPlayerEvent)event;
        Permanent p = game.getPermanent(event.getSourceId());
        return damageEvent.isCombatDamage() && p != null && filter.match(p, getSourceId(), getControllerId(), game);
    }

    @Override
    public String getRule() {
        return "Whenever a Goblin or Rogue you control deals combat damage to a player, if {this} is in your graveyard, you may return {this} to your hand.";
    }
}
