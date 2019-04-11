package ti02.robotica.PhotoDetection;

import ti02.robotica.Logging.CurrentLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Camera {

    public BufferedImage TakePicture() {
        CurrentLogger.Logger.Info("Taking picture..");
        BufferedImage output = null;
        long start = System.currentTimeMillis();

        try {
            output = ImageIO.read(new File("input/m_en_m_rood.jpeg"));

            CurrentLogger.Logger.Info("Finished taking picture. Took " + (System.currentTimeMillis() - start) + " ms.");

        } catch (IOException e) {
            CurrentLogger.Logger.Error("Error: could not take picture.");
            CurrentLogger.Logger.Error(e);
            e.printStackTrace();
        }

        return output;
    }
}
