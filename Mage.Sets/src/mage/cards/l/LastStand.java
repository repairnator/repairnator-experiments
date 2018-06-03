
package mage.cards.l;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.CreateTokenEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.filter.common.FilterControlledLandPermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.game.permanent.token.SaprolingToken;
import mage.players.Player;
import mage.target.common.TargetCreaturePermanent;
import mage.target.common.TargetOpponent;

/**
 *
 * @author LevelX2
 */
public final class LastStand extends CardImpl {

    public LastStand(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{W}{U}{B}{R}{G}");

        // Target opponent loses 2 life for each Swamp you control. Last Stand deals damage to target creature equal to the number of Mountains you control. Create a 1/1 green Saproling creature token for each Forest you control. You gain 2 life for each Plains you control. Draw a card for each Island you control, then discard that many cards.
        this.getSpellAbility().addEffect(new LastStandEffect());
        this.getSpellAbility().addTarget(new TargetOpponent());
        this.getSpellAbility().addTarget(new TargetCreaturePermanent());
    }

    public LastStand(final LastStand card) {
        super(card);
    }

    @Override
    public LastStand copy() {
        return new LastStand(this);
    }
}

class LastStandEffect extends OneShotEffect {

    private static final FilterControlledLandPermanent filterSwamp = new FilterControlledLandPermanent();
    private static final FilterControlledLandPermanent filterMountain = new FilterControlledLandPermanent();
    private static final FilterControlledLandPermanent filterPlains = new FilterControlledLandPermanent();
    private static final FilterControlledLandPermanent filterForest = new FilterControlledLandPermanent();
    private static final FilterControlledLandPermanent filterIsland = new FilterControlledLandPermanent();
    static {
        filterSwamp.add(new SubtypePredicate(SubType.SWAMP));
        filterMountain.add(new SubtypePredicate(SubType.MOUNTAIN));
        filterPlains.add(new SubtypePredicate(SubType.PLAINS));
        filterForest.add(new SubtypePredicate(SubType.FOREST));
        filterIsland.add(new SubtypePredicate(SubType.ISLAND));
    }

    public LastStandEffect() {
        super(Outcome.Benefit);
        this.staticText = "Target opponent loses 2 life for each Swamp you control. Last Stand deals damage to target creature equal to the number of Mountains you control. Create a 1/1 green Saproling creature token for each Forest you control. You gain 2 life for each Plains you control. Draw a card for each Island you control, then discard that many cards";
    }

    public LastStandEffect(final LastStandEffect effect) {
        super(effect);
    }

    @Override
    public LastStandEffect copy() {
        return new LastStandEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            // Target opponent loses 2 life for each Swamp you control
            Player opponent = game.getPlayer(getTargetPointer().getFirst(game, source));
            if (opponent != null) {
                int swamps = game.getBattlefield().count(filterSwamp, source.getSourceId(), source.getControllerId(), game);
                opponent.loseLife(swamps * 2, game, false);
            }
            // Last Stand deals damage equal to the number of Mountains you control to target creature.
            Permanent creature = game.getPermanent(source.getTargets().get(1).getFirstTarget());
            if (creature != null) {
                int mountains = game.getBattlefield().count(filterMountain, source.getSourceId(), source.getControllerId(), game);
                if (mountains > 0) {
                    creature.damage(mountains, source.getSourceId(), game, false, true);
                }
            }
            // Create a 1/1 green Saproling creature token for each Forest you control.
            int forests = game.getBattlefield().count(filterForest, source.getSourceId(), source.getControllerId(), game);
            if (forests > 0) {
                new CreateTokenEffect(new SaprolingToken(), forests).apply(game, source);
            }
            // You gain 2 life for each Plains you control.
            int plains = game.getBattlefield().count(filterPlains, source.getSourceId(), source.getControllerId(), game);
            controller.gainLife(plains * 2, game, source);
            // Draw a card for each Island you control, then discard that many cards
            int islands = game.getBattlefield().count(filterIsland, source.getSourceId(), source.getControllerId(), game);
            if (islands > 0) {
                controller.drawCards(islands, game);
                controller.discard(islands, false, source, game);
            }

        }
        return false;
    }
}
