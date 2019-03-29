package ti02.robotica.PhotoDetection;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.Exposure;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import ti02.robotica.Logging.CurrentLogger;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Camera {

    public BufferedImage TakePicture() {
        BufferedImage output = null;

        try {
            // https://github.com/Hopding/JRPiCam
            RPiCamera piCamera = new RPiCamera("/tmp");

            piCamera.setWidth(512).setHeight(512)   // Set Camera to produce 500x500 images.
                    .setBrightness(75)              // Adjust Camera's brightness setting.
                    .setExposure(Exposure.SPORTS)   // Set Camera's exposure.
                    .setTimeout(2)                  // Set Camera's timeout.
                    .setAddRawBayer(true);          // Add Raw Bayer data to image files created by Camera.

            output = piCamera.takeBufferedStill();

        } catch (FailedToRunRaspistillException | InterruptedException | IOException e) {
            CurrentLogger.Logger.Error("Error: could not take picture.");
            CurrentLogger.Logger.Error(e);
            e.printStackTrace();
        }

        return output;
    }
}