package ti02.robotica.PhotoDetection;

import ti02.robotica.Logging.CurrentLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Camera {

    public BufferedImage TakePicture() {
        BufferedImage read = null;

        try {
            read = ImageIO.read(new File("input/m_en_m_rood.jpeg"));
        } catch (IOException e) {
            CurrentLogger.Logger.Error(e);
        }

        return read;
    }

}
