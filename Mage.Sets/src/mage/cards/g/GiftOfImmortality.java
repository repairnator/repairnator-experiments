
package mage.cards.g;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.common.DiesAttachedTriggeredAbility;
import mage.abilities.common.delayed.AtTheBeginOfNextEndStepDelayedTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.AttachEffect;
import mage.abilities.keyword.EnchantAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.TargetPermanent;
import mage.target.common.TargetCreaturePermanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX2
 */
public final class GiftOfImmortality extends CardImpl {

    public GiftOfImmortality(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ENCHANTMENT}, "{2}{W}");
        this.subtype.add(SubType.AURA);

        // Enchant creature
        TargetPermanent auraTarget = new TargetCreaturePermanent();
        this.getSpellAbility().addTarget(auraTarget);
        this.getSpellAbility().addEffect(new AttachEffect(Outcome.AddAbility));
        Ability ability = new EnchantAbility(auraTarget.getTargetName());
        this.addAbility(ability);

        // When enchanted creature dies, return that card to the battlefield under its owner's control.
        // Return Gift of Immortality to the battlefield attached to that creature at the beginning of the next end step.
        this.addAbility(new DiesAttachedTriggeredAbility(new GiftOfImmortalityEffect(), "enchanted creature", false));
    }

    public GiftOfImmortality(final GiftOfImmortality card) {
        super(card);
    }

    @Override
    public GiftOfImmortality copy() {
        return new GiftOfImmortality(this);
    }
}

class GiftOfImmortalityEffect extends OneShotEffect {

    public GiftOfImmortalityEffect() {
        super(Outcome.Benefit);
        this.staticText = "return that card to the battlefield under its owner's control. Return {this} to the battlefield attached to that creature at the beginning of the next end step";
    }

    public GiftOfImmortalityEffect(final GiftOfImmortalityEffect effect) {
        super(effect);
    }

    @Override
    public GiftOfImmortalityEffect copy() {
        return new GiftOfImmortalityEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent enchantment = (Permanent) game.getLastKnownInformation(source.getSourceId(), Zone.BATTLEFIELD);
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null && enchantment != null && enchantment.getAttachedTo() != null) {
            Permanent enchanted = (Permanent) game.getLastKnownInformation(enchantment.getAttachedTo(), Zone.BATTLEFIELD);
            Card card = game.getCard(enchantment.getAttachedTo());
            if (card != null && enchanted != null && card.getZoneChangeCounter(game) == enchanted.getZoneChangeCounter(game) + 1) {
                controller.moveCards(card, Zone.BATTLEFIELD, source, game, false, false, true, null);
                Permanent permanent = game.getPermanent(card.getId());
                if (permanent != null) {
                    //create delayed triggered ability
                    Effect effect = new GiftOfImmortalityReturnEnchantmentEffect();
                    effect.setTargetPointer(new FixedTarget(permanent, game));
                    game.addDelayedTriggeredAbility(new AtTheBeginOfNextEndStepDelayedTriggeredAbility(effect), source);
                }

            }
            return true;
        }

        return false;
    }

}

class GiftOfImmortalityReturnEnchantmentEffect extends OneShotEffect {

    public GiftOfImmortalityReturnEnchantmentEffect() {
        super(Outcome.PutCardInPlay);
        staticText = "Return {this} to the battlefield attached to that creature at the beginning of the next end step";
    }

    public GiftOfImmortalityReturnEnchantmentEffect(final GiftOfImmortalityReturnEnchantmentEffect effect) {
        super(effect);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Card aura = game.getCard(source.getSourceId());
        if (aura != null && game.getState().getZone(aura.getId()) == Zone.GRAVEYARD) {
            Player controller = game.getPlayer(source.getControllerId());
            Permanent creature = game.getPermanent(getTargetPointer().getFirst(game, source));
            if (controller != null && creature != null) {
                game.getState().setValue("attachTo:" + aura.getId(), creature);
                controller.moveCards(aura, Zone.BATTLEFIELD, source, game);
                return creature.addAttachment(aura.getId(), game);
            }
        }

        return false;
    }

    @Override
    public GiftOfImmortalityReturnEnchantmentEffect copy() {
        return new GiftOfImmortalityReturnEnchantmentEffect(this);
    }
}
