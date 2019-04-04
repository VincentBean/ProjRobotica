package ti02.robotica.Models;

import ti02.robotica.Detector.IDetector;

import java.util.Map;

public class Object {
    private int[][] Pixels;
    private Bounds Bounds;
    private Map<String, DetectorResult> Properties;

    public void RunDetector(IDetector detector) {
        DetectorResult result = detector.detect();

        Properties.put(detector.toString(), result);
    }


}
