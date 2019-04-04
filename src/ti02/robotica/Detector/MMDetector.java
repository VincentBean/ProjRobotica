package ti02.robotica.Detector;

import ti02.robotica.Models.DetectorResult;
import ti02.robotica.Models.Object;

import java.util.List;

public class MMDetector implements IDetector {
    @Override
    public DetectorResult<List<Object>> detect() {
        return null;
    }

    /**
     * Returns the name of this detector
     */
    @Override
    public String toString() {
        Class<?> enclosingClass = getClass().getEnclosingClass();
        String name;

        if (enclosingClass != null) {
            name = enclosingClass.getName();
        } else {
            name = getClass().getName();
        }

        return name.replace("Detector", "");
    }
}
