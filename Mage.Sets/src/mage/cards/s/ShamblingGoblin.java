
package mage.cards.s;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.DiesTriggeredAbility;
import mage.abilities.effects.common.continuous.BoostTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Duration;
import mage.constants.TargetController;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author LevelX2
 */
public final class ShamblingGoblin extends CardImpl {

    private static final FilterCreaturePermanent filterOpponentCreature = new FilterCreaturePermanent("creature an opponent controls");

    static {
        filterOpponentCreature.add(new ControllerPredicate(TargetController.OPPONENT));
    }

    public ShamblingGoblin(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{B}");
        this.subtype.add(SubType.ZOMBIE);
        this.subtype.add(SubType.GOBLIN);
        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        // When Shambling Goblin dies, target creature an opponent controls gets -1/-1 until end of turn.
        Ability ability = new DiesTriggeredAbility(new BoostTargetEffect(-1,-1, Duration.EndOfTurn));
        ability.addTarget(new TargetCreaturePermanent(filterOpponentCreature));
        this.addAbility(ability);

    }

    public ShamblingGoblin(final ShamblingGoblin card) {
        super(card);
    }

    @Override
    public ShamblingGoblin copy() {
        return new ShamblingGoblin(this);
    }
}
