package ti02.robotica;


import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.PhotoDetection.Camera;
import ti02.robotica.PhotoDetection.PictureProcessor;

public class MMSorter {
    public static void main(String[] args) {
        Camera camera = new Camera();
        PictureProcessor pictureProcessor = new PictureProcessor();

        MMPicture mmPicture = pictureProcessor.findMM(camera.TakePicture());

        CurrentLogger.Logger.Info(mmPicture.getColorPixels().size() + "");
    }
}
