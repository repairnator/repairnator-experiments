/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.geospatial;

import com.facebook.presto.spi.PrestoException;

import static com.facebook.presto.spi.StandardErrorCode.INVALID_FUNCTION_ARGUMENT;
import static com.google.common.base.Preconditions.checkArgument;

public final class BingTile
{
    public static final int MAX_ZOOM_LEVEL = 23;

    private final int x;
    private final int y;
    private final int zoomLevel;

    private BingTile(int x, int y, int zoomLevel)
    {
        checkArgument(zoomLevel <= MAX_ZOOM_LEVEL);
        this.x = x;
        this.y = y;
        this.zoomLevel = zoomLevel;
    }

    public static BingTile fromCoordinates(int x, int y, int zoomLevel)
    {
        return new BingTile(x, y, zoomLevel);
    }

    public static BingTile fromQuadKey(String quadKey)
    {
        int zoomLevel = quadKey.length();
        checkArgument(zoomLevel <= MAX_ZOOM_LEVEL);
        int tileX = 0;
        int tileY = 0;
        for (int i = zoomLevel; i > 0; i--) {
            int mask = 1 << (i - 1);
            switch (quadKey.charAt(zoomLevel - i)) {
                case '0':
                    break;
                case '1':
                    tileX |= mask;
                    break;
                case '2':
                    tileY |= mask;
                    break;
                case '3':
                    tileX |= mask;
                    tileY |= mask;
                    break;
                default:
                    throw new PrestoException(INVALID_FUNCTION_ARGUMENT, "Invalid QuadKey digit sequence: " + quadKey);
            }
        }

        return new BingTile(tileX, tileY, zoomLevel);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZoomLevel()
    {
        return zoomLevel;
    }

    public String toQuadKey()
    {
        char[] quadKey = new char[this.zoomLevel];
        for (int i = this.zoomLevel; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((this.x & mask) != 0) {
                digit++;
            }
            if ((this.y & mask) != 0) {
                digit += 2;
            }
            quadKey[this.zoomLevel - i] = digit;
        }
        return String.valueOf(quadKey);
    }

    /**
     * Encodes Bing tile as a 64-bit long: 23 bits for X, followed by 23 bits for Y,
     * followed by 5 bits for zoomLevel
     */
    public long encode()
    {
        return (((long) x) << 28) + (y << 5) + zoomLevel;
    }

    public static BingTile decode(long tile)
    {
        int tileX = (int) (tile >> 28);
        int tileY = (int) ((tile % (1 << 28)) >> 5);
        int zoomLevel = (int) (tile % (1 << 5));

        return new BingTile(tileX, tileY, zoomLevel);
    }
}
