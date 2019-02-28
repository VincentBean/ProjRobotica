package ti02.robotica;


import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.PhotoDetection.Camera;
import ti02.robotica.PhotoDetection.PictureProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MMSorter {
    public static void main(String[] args) {
        Camera camera = new Camera();
        PictureProcessor pictureProcessor = new PictureProcessor();

        MMPicture mmPicture = pictureProcessor.findMM(camera.TakePicture());

        CurrentLogger.Logger.Info(mmPicture.getColorPixels().size() + "");



        JPanel panel = new JPanel();

        JLabel label = new JLabel(new ImageIcon(camera.TakePicture()));
        panel.add(label);
        JLabel label2 = new JLabel(new ImageIcon(mmPicture.toBufferedImage()));
        panel.add(label2);

        // main window
        JFrame frame = new JFrame("JPanel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the Jpanel to the main window
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

    }
}
