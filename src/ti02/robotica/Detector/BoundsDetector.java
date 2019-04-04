package ti02.robotica.Detector;

import ti02.robotica.Models.Bounds;
import ti02.robotica.Models.DetectorResult;

public class BoundsDetector implements IDetector {
    @Override
    public DetectorResult<Bounds> detect() {
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
