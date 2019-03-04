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

        BufferedImage image = camera.TakePicture();
        BufferedImage overlay = mmPicture.toBufferedImage();

        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(image.getWidth(), overlay.getWidth());
        int h = Math.max(image.getHeight(), overlay.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

// paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(overlay, 0, 0, null);


        JPanel panel = new JPanel();

        JLabel label = new JLabel(new ImageIcon(combined));
        panel.add(label);
        JLabel label2 = new JLabel(new ImageIcon(overlay));
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
