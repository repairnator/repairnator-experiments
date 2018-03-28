/* Copyright (c) 2014 Varun Pant
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * original code retrieved from
 * https://github.com/varunpant/GHEAT-JAVA
 *
 * The code was modified by the IDSC-Frazzoli team at the
 * Institute for Dynamic Systems and Control of ETH Zurich 
 * for use in the amodeus library, 2017-2018. */

package ch.ethz.idsc.amodeus.view.gheat;

import java.awt.image.BufferedImage;

import ch.ethz.idsc.amodeus.view.gheat.gui.ColorScheme;

public enum HeatMap {
    ;

    public static final int SIZE = 256;
    public static final int MAX_ZOOM = 31;

    public static BufferedImage getTile( //
            DataManager dataManager, //
            ColorScheme colorScheme, //
            int zoom, int x, int y) throws Exception {
        if (dataManager == null)
            throw new Exception("No DataManager specified");
        return Tile.generate( //
                colorScheme, //
                DotImages.get(zoom), //
                zoom, //
                x, //
                y, //
                dataManager.getPointsForTile(x, y, DotImages.get(zoom).bufferedImageRGB, zoom));
    }
}
