// License: GPL. For details, see Readme.txt file.
package ch.ethz.idsc.amodeus.view.jmapviewer.interfaces;

import ch.ethz.idsc.amodeus.view.jmapviewer.Tile;

/** This listener listens to successful tile loads. */
@FunctionalInterface
public interface TileLoaderListener {

    /** Will be called if a new {@link Tile} has been loaded successfully. Loaded
     * can mean downloaded or loaded from file cache.
     *
     * @param tile
     *            The tile
     * @param success
     *            {@code true} if the tile has been loaded successfully,
     *            {@code false} otherwise */
    void tileLoadingFinished(Tile tile, boolean success);
}
