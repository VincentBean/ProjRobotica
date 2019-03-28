package ti02.robotica.PhotoDetection;

import com.github.sarxos.webcam.Webcam;
import ti02.robotica.Logging.CurrentLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Camera {

    public BufferedImage TakePicture() {
        BufferedImage read = null;

        List<Webcam> cams = Webcam.getWebcams();

        Webcam cam = cams.get(1);

        cam.open();

        CurrentLogger.Logger.Debug("Cams found: " + cams.size());

        for (int i = 0; i < cams.size(); i++) {
            CurrentLogger.Logger.Debug("Cam " + i + ": " + cams.get(i).getName());
        }

        BufferedImage img =  cam.getImage();

        return img;
        /*
        try {
            read = ImageIO.read(new File("input/m_en_m_rood.jpeg"));
        } catch (IOException e) {
            CurrentLogger.Logger.Error(e);
        }

        return read;*/
    }

}
