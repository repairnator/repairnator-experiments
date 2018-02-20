package mars.out;

import mars.algorithm.Algorithm;
import mars.coordinate.Coordinate;
import mars.views.MapFrame;

import java.util.List;

/**
 * Class which draws a discovered path on an image of a map.
 */
public class MapImageOutput extends Output {

    private String mapPath;

    /**
     * Constructor for MapImageOutput.
     * It immediately displays the output.
     * @param out list of coordinates generated by program
     */
    public MapImageOutput(List<? extends Coordinate> out, String path) {
        resultList = out;
        mapPath = path;
        writeToOutput();
    }

    /**
     * Constructor for MapImageOutput which takes an Algorithm.
     * It pulls the discovered path and map file from the algorithm and immediately displays the output.
     * @param algorithm The completed algorithm which stores the discovered path and the map file.
     */
    public MapImageOutput(Algorithm algorithm) {
        resultList = algorithm.getPath();
        mapPath = algorithm.map.getMapPath();
        writeToOutput();
    }

    /**
     * Function that displays the image of the map with the path drawn on it.
     */
    public void writeToOutput() {
        new MapFrame(resultList, mapPath);
    }
}
