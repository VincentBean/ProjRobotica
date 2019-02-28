package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try
        {
            BufferedImage picture = ImageIO.read(new File("m_en_m_rood.jpeg"));
            BufferedImage output = new BufferedImage(picture.getWidth(), picture.getHeight(), picture.getType());

            for (int x = 0; x < picture.getWidth(); x++)
            {
                for (int y = 0; y < picture.getHeight(); y++)
                {
                    Color color = new Color(picture.getRGB(x, y));
                    int rgbIN[] = {color.getRed(), color.getGreen(), color.getBlue()};

                    int[] rgbOUT = {0, 0, 0};

                    if (rgbIN[0] >= 200) {
                        rgbOUT[0] = 255;
                        rgbOUT[2] = 255;
                    }

                    output.setRGB(x, y, new Color(rgbOUT[0], rgbOUT[1], rgbOUT[2]).getRGB());
                }
            }

            ImageIO.write(output, "jpg", new File("m_en_m_rood_converted.jpeg"));
        }
        catch (IOException e)
        {
            String workingDir = System.getProperty("user.dir");
            System.out.println("Current working directory : " + workingDir);
            e.printStackTrace();
        }
    }
}
