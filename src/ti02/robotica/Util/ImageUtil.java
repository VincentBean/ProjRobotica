package ti02.robotica.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

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

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /**
     * Create a BufferedImage based on a array of pixels
     *
     * @param pixelData
     * @param outputImage
     * @return
     */
    public static BufferedImage createImage(int[][] pixelData, BufferedImage outputImage)
    {
        int[] outputImagePixelData = ((DataBufferInt) outputImage.getRaster().getDataBuffer()).getData() ;

        final int width = outputImage.getWidth() ;
        final int height = outputImage.getHeight() ;

        for (int y=0, pos=0 ; y < height ; y++)
            for (int x=0 ; x < width ; x++, pos++)
                outputImagePixelData[pos] = pixelData[y][x];

        return outputImage;
    }
}
