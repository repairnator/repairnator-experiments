
package mage.cards.b;

import java.util.UUID;
import mage.abilities.effects.common.search.SearchLibraryPutInHandEffect;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.filter.FilterCard;
import mage.filter.common.FilterControlledLandPermanent;
import mage.filter.predicate.Predicate;
import mage.game.Game;
import mage.target.common.TargetCardInLibrary;

/**
 *
 * @author Plopman
 */
public final class BeseechTheQueen extends CardImpl {

    private static final FilterCard filter = new FilterCard("card with converted mana cost less than or equal to the number of lands you control");
    static{
        filter.add(new BeseechTheQueenPredicate());
    }
    public BeseechTheQueen(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.SORCERY},"{2/B}{2/B}{2/B}");


        // <i>({2B} can be paid with any two mana or with {B}. This card's converted mana cost is 6.)</i>
        // Search your library for a card with converted mana cost less than or equal to the number of lands you control, reveal it, and put it into your hand. Then shuffle your library.
        this.getSpellAbility().addEffect(new SearchLibraryPutInHandEffect(new TargetCardInLibrary(filter), true, true));
    }

    public BeseechTheQueen(final BeseechTheQueen card) {
        super(card);
    }

    @Override
    public BeseechTheQueen copy() {
        return new BeseechTheQueen(this);
    }
}


class BeseechTheQueenPredicate implements Predicate<Card> {


    @Override
    public final boolean apply(Card input, Game game) {
        if(input.getConvertedManaCost() <= game.getBattlefield().getAllActivePermanents(new FilterControlledLandPermanent(), input.getOwnerId(), game).size()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "card with converted mana cost less than or equal to the number of lands you control";
    }
}