/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */

package mage.abilities.common;

import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.Effect;
import mage.constants.Zone;
import mage.filter.FilterPermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.ZoneChangeEvent;
import mage.game.permanent.Permanent;

/**
 * Is applied when a {@link Permanent} matching the filter changes zones.
 *
 * @author LevelX2
 */
public class ZoneChangeAllTriggeredAbility extends TriggeredAbilityImpl {

    protected FilterPermanent filter;
    protected Zone fromZone;
    protected Zone toZone;
    protected String rule;

    public ZoneChangeAllTriggeredAbility(Zone zone, Zone toZone, Effect effect, FilterPermanent filter, String rule, boolean optional) {
        this(zone, null, toZone, effect, filter, rule, optional);
    }

    public ZoneChangeAllTriggeredAbility(Zone zone, Zone fromZone, Zone toZone, Effect effect, FilterPermanent filter, String rule, boolean optional) {
        super(zone, effect, optional);
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.rule = rule;
        this.filter = filter;
    }

    public ZoneChangeAllTriggeredAbility(final ZoneChangeAllTriggeredAbility ability) {
        super(ability);
        this.fromZone = ability.fromZone;
        this.toZone = ability.toZone;
        this.rule = ability.rule;
        this.filter = ability.filter;
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == GameEvent.EventType.ZONE_CHANGE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        ZoneChangeEvent zEvent = (ZoneChangeEvent)event;
        if ((fromZone == null || zEvent.getFromZone() == fromZone) && (toZone == null || zEvent.getToZone() == toZone)) {
            Permanent perm;
            if (zEvent.getTarget() != null) {
                perm = zEvent.getTarget();
            } else {
                perm = game.getPermanent(event.getTargetId()); // LevelX2: maybe this part is not neccessary
            }
            if (perm != null && filter.match(perm, sourceId, controllerId, game)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return rule + super.getRule();
    }

    @Override
    public ZoneChangeAllTriggeredAbility copy() {
        return new ZoneChangeAllTriggeredAbility(this);
    }

    public Zone getFromZone() {
        return fromZone;
    }

    public Zone getToZone() {
        return toZone;
    }
}
