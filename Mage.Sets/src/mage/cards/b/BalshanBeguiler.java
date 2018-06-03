
package mage.cards.b;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DealsCombatDamageToAPlayerTriggeredAbility;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.CardsImpl;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.TargetCard;

/**
 *
 * @author cbt33, noxx (DiscardCardYouChooseTargetOpponentEffect)
 */
public final class BalshanBeguiler extends CardImpl {

    public BalshanBeguiler(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{2}{U}");
        this.subtype.add(SubType.HUMAN, SubType.WIZARD);

        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        // Whenever Balshan Beguiler deals combat damage to a player, that player reveals the top two cards of their library. You choose one of those cards and put it into their graveyard.
        this.addAbility(new DealsCombatDamageToAPlayerTriggeredAbility(new BalshanBeguilerEffect(), false, true));
    }

    public BalshanBeguiler(final BalshanBeguiler card) {
        super(card);
    }

    @Override
    public BalshanBeguiler copy() {
        return new BalshanBeguiler(this);
    }
}

class BalshanBeguilerEffect extends OneShotEffect {
    
    public BalshanBeguilerEffect() {
        super(Outcome.Benefit);
        this.staticText = " that player reveals the top two cards of their library. You choose one of those cards and put it into their graveyard.";
    }
    
    public BalshanBeguilerEffect(final BalshanBeguilerEffect effect) {
        super(effect);
    }
    
    @Override
    public BalshanBeguilerEffect copy() {
        return new BalshanBeguilerEffect(this);
    }
    
    @Override
    public boolean apply(Game game, Ability source) {
        Player player = game.getPlayer(source.getFirstTarget());
        if (player != null) {
            CardsImpl cards = new CardsImpl();
            cards.addAll(player.getLibrary().getTopCards(game, 2));
            Player you = game.getPlayer(source.getControllerId());
            if (you != null) {
                TargetCard target = new TargetCard(Zone.LIBRARY, new FilterCard());
                if (you.choose(Outcome.Benefit, cards, target, game)) {
                    Card card = player.getLibrary().getCard(target.getFirstTarget(), game);
                    card.moveToZone(Zone.BATTLEFIELD, source.getSourceId(), game, true);
                }
            }
        }
        return false;
    }
}
