/* amodeus - Copyright (c) 2018, ETH Zurich, Institute for Dynamic Systems and Control */
package ch.ethz.idsc.amodeus.prep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;

import ch.ethz.idsc.amodeus.util.math.GlobalAssert;

/** example use:
 *
 * TheApocalypse.reducesThe(population).toNoMoreThan(1000).people(); */
public final class TheApocalypse {
    public static TheApocalypse reducesThe(Population population) {
        return new TheApocalypse(population);
    }

    private final Population population;

    private TheApocalypse(Population population) {
        this.population = population;
    }

    /** version with seed used so far **/
    public TheApocalypse toNoMoreThan(int capacityOfArk) {
        return toNoMoreThan(capacityOfArk, 7582456789l);
    }

    public TheApocalypse toNoMoreThan(int capacityOfArk, long seed) {
        List<Id<Person>> list = new ArrayList<>(population.getPersons().keySet());
        Collections.shuffle(list, new Random(7582456789l));
        final int sizeAnte = list.size();
        list.stream() //
                .limit(Math.max(0, sizeAnte - capacityOfArk)) //
                .forEach(population::removePerson);
        final int sizePost = population.getPersons().size();
        GlobalAssert.that(sizePost <= capacityOfArk);
        return this;
    }

    public final void people() {
        System.out.println("Population size: " + population.getPersons().values().size());
    }
}
