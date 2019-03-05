package ti02.robotica.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static BufferedImage Combine(BufferedImage src1, BufferedImage src2)
    {
        // create the new image, canvas size is the max. of both image sizes
        int w = Math.max(src1.getWidth(), src2.getWidth());
        int h = Math.max(src1.getHeight(), src2.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(src1, 0, 0, null);
        g.drawImage(src2, 0, 0, null);

        return combined;
    }
}
