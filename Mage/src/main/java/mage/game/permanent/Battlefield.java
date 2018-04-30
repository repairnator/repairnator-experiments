/*
 * Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of BetaSteward_at_googlemail.com.
 */
package mage.game.permanent;

import mage.abilities.keyword.PhasingAbility;
import mage.constants.CardType;
import mage.constants.RangeOfInfluence;
import mage.filter.FilterPermanent;
import mage.game.Game;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author BetaSteward_at_googlemail.com
 */
public class Battlefield implements Serializable {

    private final Map<UUID, Permanent> field = new LinkedHashMap<>();

    public Battlefield() {
    }

    public Battlefield(final Battlefield battlefield) {
        for (Entry<UUID, Permanent> entry : battlefield.field.entrySet()) {
            field.put(entry.getKey(), entry.getValue().copy());
        }
    }

    public Battlefield copy() {
        return new Battlefield(this);
    }

    public void reset(Game game) {
        for (Permanent perm : field.values()) {
            perm.reset(game);
        }
    }

    public void clear() {
        field.clear();
    }

    /**
     * Returns a count of all {@link Permanent} that match the filter and are
     * controlled by controllerId.
     * <p>
     * Some filter predicates do not work here (e.g. AnotherPredicate() because
     * filter.match() is called without controllerId. To use this predicates you
     * can use count() instead of countAll()
     *
     * @param filter
     * @param controllerId
     * @param game
     * @return count
     */
    public int countAll(FilterPermanent filter, UUID controllerId, Game game) {
        return (int) field.values()
                .stream()
                .filter(permanent -> permanent.getControllerId().equals(controllerId)
                        && filter.match(permanent, game)
                        && permanent.isPhasedIn())
                .count();

    }

    /**
     * Returns a count of all {@link Permanent} that are within the range of
     * influence of the specified player id and that match the supplied filter.
     *
     * @param filter
     * @param sourceId       - sourceId of the MageObject the calling effect/ability
     *                       belongs to
     * @param sourcePlayerId
     * @param game
     * @return count
     */
    public int count(FilterPermanent filter, UUID sourceId, UUID sourcePlayerId, Game game) {
        if (game.getRangeOfInfluence() == RangeOfInfluence.ALL) {
            return (int) field.values()
                    .stream()
                    .filter(permanent -> filter.match(permanent, sourceId, sourcePlayerId, game)
                            && permanent.isPhasedIn())
                    .count();
        } else {
            Set<UUID> range = game.getPlayer(sourcePlayerId).getInRange();
            return  (int) field.values()
                    .stream()
                    .filter(permanent -> range.contains(permanent.getControllerId())
                            && filter.match(permanent, sourceId, sourcePlayerId, game)
                            && permanent.isPhasedIn()).count();
        }
    }

    /**
     * Returns true if the battlefield contains at least 1 {@link Permanent}
     * that matches the filter. This method ignores the range of influence.
     *
     * @param filter
     * @param num
     * @param game
     * @return boolean
     */
    public boolean contains(FilterPermanent filter, int num, Game game) {
        return field.values()
                .stream()
                .filter(permanent -> filter.match(permanent, game)
                        && permanent.isPhasedIn()).count() >= num;

    }

    /**
     * Returns true if the battlefield contains num or more {@link Permanent}
     * that matches the filter and is controlled by controllerId. This method
     * ignores the range of influence.
     *
     * @param filter
     * @param controllerId
     * @param num
     * @param game
     * @return boolean
     */
    public boolean contains(FilterPermanent filter, UUID controllerId, int num, Game game) {
        return field.values()
                .stream()
                .filter(permanent -> permanent.getControllerId().equals(controllerId)
                        && filter.match(permanent, game)
                        && permanent.isPhasedIn())
                .count() >= num;

    }

    /**
     * Returns true if the battlefield contains num or more {@link Permanent}
     * that is within the range of influence of the specified player id and that
     * matches the supplied filter.
     *
     * @param filter
     * @param sourcePlayerId
     * @param game
     * @param num
     * @return boolean
     */
    public boolean contains(FilterPermanent filter, UUID sourcePlayerId, Game game, int num) {
        if (game.getRangeOfInfluence() == RangeOfInfluence.ALL) {
            return field.values().stream()
                    .filter(permanent -> filter.match(permanent, null, sourcePlayerId, game)
                            && permanent.isPhasedIn()).count() >= num;

        } else {
            Set<UUID> range = game.getPlayer(sourcePlayerId).getInRange();
            return field.values().stream()
                    .filter(permanent -> range.contains(permanent.getControllerId())
                            && filter.match(permanent, null, sourcePlayerId, game)
                            && permanent.isPhasedIn())
                    .count() >= num;
        }
    }

    public void addPermanent(Permanent permanent) {
        field.put(permanent.getId(), permanent);
    }

    public Permanent getPermanent(UUID key) {
        return field.get(key);
    }

    public void removePermanent(UUID key) {
        field.remove(key);
    }

    public boolean containsPermanent(UUID key) {
        return field.containsKey(key);
    }

    public void beginningOfTurn(Game game) {
        for (Permanent perm : field.values()) {
            perm.beginningOfTurn(game);
        }
    }

    public void endOfTurn(UUID controllerId, Game game) {
        for (Permanent perm : field.values()) {
            perm.endOfTurn(game);
        }
    }

    public Collection<Permanent> getAllPermanents() {
        return field.values();
    }

    public Set<UUID> getAllPermanentIds() {
        return field.keySet();
    }

    public List<Permanent> getAllActivePermanents() {
        return field.values()
                .stream()
                .filter(Permanent::isPhasedIn)
                .collect(Collectors.toList());
    }

    /**
     * Returns all {@link Permanent} on the battlefield that are controlled by
     * the specified player id. The method ignores the range of influence.
     *
     * @param controllerId
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getAllActivePermanents(UUID controllerId) {
        return field.values()
                .stream()
                .filter(perm -> perm.isPhasedIn()
                        && perm.getControllerId().equals(controllerId))
                .collect(Collectors.toList());
    }

    /**
     * Returns all {@link Permanent} on the battlefield that match the specified
     * {@link CardType}. This method ignores the range of influence.
     *
     * @param type
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getAllActivePermanents(CardType type) {
        return field.values()
                .stream()
                .filter(perm -> perm.isPhasedIn() && perm.getCardType().contains(type))
                .collect(Collectors.toList());
    }

    /**
     * Returns all {@link Permanent} on the battlefield that match the supplied
     * filter. This method ignores the range of influence.
     *
     * @param filter
     * @param game
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getAllActivePermanents(FilterPermanent filter, Game game) {
        return field.values()
                .stream()
                .filter(perm -> perm.isPhasedIn() && filter.match(perm, game))
                .collect(Collectors.toList());
    }

    /**
     * Returns all {@link Permanent} that match the filter and are controlled by
     * controllerId. This method ignores the range of influence.
     *
     * @param filter
     * @param controllerId
     * @param game
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getAllActivePermanents(FilterPermanent filter, UUID controllerId, Game game) {
        return field.values()
                .stream()
                .filter(perm -> perm.isPhasedIn() && perm.getControllerId().equals(controllerId) && filter.match(perm, game))
                .collect(Collectors.toList());
    }

    /**
     * Returns all {@link Permanent} that are within the range of influence of
     * the specified player id and that match the supplied filter.
     *
     * @param filter
     * @param sourcePlayerId
     * @param game
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getActivePermanents(FilterPermanent filter, UUID sourcePlayerId, Game game) {
        return getActivePermanents(filter, sourcePlayerId, null, game);
    }

    /**
     * Returns all {@link Permanent} that are within the range of influence of
     * the specified player id and that match the supplied filter.
     *
     * @param filter
     * @param sourcePlayerId
     * @param sourceId
     * @param game
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getActivePermanents(FilterPermanent filter, UUID sourcePlayerId, UUID sourceId, Game game) {
        if (game.getRangeOfInfluence() == RangeOfInfluence.ALL) {
            return field.values()
                    .stream()
                    .filter(perm -> perm.isPhasedIn() && filter.match(perm, sourceId, sourcePlayerId, game))
                    .collect(Collectors.toList());
        } else {
            Set<UUID> range = game.getPlayer(sourcePlayerId).getInRange();
            return  field.values()
                    .stream()
                    .filter(perm -> perm.isPhasedIn() && range.contains(perm.getControllerId())
                            && filter.match(perm, sourceId, sourcePlayerId, game)).collect(Collectors.toList());
        }
    }

    /**
     * Returns all {@link Permanent} that are within the range of influence of
     * the specified player id.
     *
     * @param sourcePlayerId
     * @param game
     * @return a list of {@link Permanent}
     * @see Permanent
     */
    public List<Permanent> getActivePermanents(UUID sourcePlayerId, Game game) {
        if (game.getRangeOfInfluence() == RangeOfInfluence.ALL) {
            return getAllActivePermanents();
        } else {
            Set<UUID> range = game.getPlayer(sourcePlayerId).getInRange();
            return field.values()
                    .stream()
                    .filter(perm -> perm.isPhasedIn()
                            && range.contains(perm.getControllerId()))
                    .collect(Collectors.toList());

        }
    }

    public List<Permanent> getPhasedIn(UUID controllerId) {
        return field.values()
                .stream()
                .filter(perm -> perm.getAbilities().containsKey(PhasingAbility.getInstance().getId())
                        && perm.isPhasedIn() &&
                        perm.getControllerId().equals(controllerId))
                .collect(Collectors.toList());
    }

    public List<Permanent> getPhasedOut(UUID controllerId) {
        return field.values()
                .stream()
                .filter(perm -> !perm.isPhasedIn() && perm.getControllerId().equals(controllerId))
                .collect(Collectors.toList());
    }

    public void resetPermanentsControl() {
        for (Permanent perm : field.values()) {
            if (perm.isPhasedIn()) {
                perm.resetControl();
            }
        }
    }

    /**
     * since control could change several times during applyEvents we only want
     * to fire control changed events after all control change effects have been
     * applied
     *
     * @param game
     * @return
     */
    public boolean fireControlChangeEvents(Game game) {
        boolean controlChanged = false;
        for (Permanent perm : field.values()) {
            if (perm.isPhasedIn()) {
                controlChanged |= perm.checkControlChanged(game);
            }
        }
        return controlChanged;
    }

}
