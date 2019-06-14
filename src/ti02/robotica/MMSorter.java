package ti02.robotica;


import ti02.robotica.Logging.CurrentLogger;
import ti02.robotica.Models.MMPicture;
import ti02.robotica.PhotoDetection.Camera;
import ti02.robotica.PhotoDetection.PictureProcessor;
import ti02.robotica.Util.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.demos.VideoImage;

public class MMSorter {
    private static VideoImage mScreenProcessed = null;
    private static VideoImage mScreenSource = null;
    private static Camera camera;
    private static PictureProcessor pictureProcessor;
    private static long previousTimestamp = System.currentTimeMillis();

    private static IMediaListener mediaListener = new MediaListenerAdapter() {
        @Override
        public void onVideoPicture(IVideoPictureEvent event) {
            try {
                BufferedImage image = event.getImage();
                if (image != null)
                    updateJavaWindow(image);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        camera = new Camera();
        pictureProcessor = new PictureProcessor();

//        IMediaReader mediaReader = ToolFactory.makeReader("rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov");
        IMediaReader mediaReader = ToolFactory.makeReader("input/mms.mp4");

//        IMediaReader mediaReader = ToolFactory.makeReader("rtsp://raspberrypi.local:8554/test.sdp");
//        $ raspivid -o - -t 0 -hf -w 640 -h 360 -fps 25 | cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://127.0.0.1:8554/test.sdp}' :demux=h264
//        $ vlc -vvv "https://www.youtube.com/watch?v=dQw4w9WgXcQ&fmt=18" --sout '#rtp{dst=127.0.0.1,port=8554,sdp=rtsp://localhost:8554/test.sdp}'

        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        mediaReader.setQueryMetaData(false);
        mediaReader.addListener(mediaListener);
        openJavaWindow();

        while(true) {
            // Reading packet
            IError err = null;
            if (mediaReader != null)
                err = mediaReader.readPacket();
            // End of packet
            if(err != null ){
                System.out.println("Error: " + err);
                break;
            }
        }
        closeJavaWindow();
    }

    private static void updateJavaWindow(BufferedImage inputImage)
    {
        mScreenSource.setImage(inputImage);
        mScreenSource.setLocation(inputImage.getWidth(),0);

        MMPicture mmPicture = pictureProcessor.findMM(inputImage);

//        CurrentLogger.Logger.Info(mmPicture.getPixelCount() + "");

        MMPictureRenderer renderer = new MMPictureRenderer();

        BufferedImage processed = renderer.render(mmPicture);
        mScreenProcessed.setImage(processed);

        CurrentLogger.Logger.Info(1000/(System.currentTimeMillis() - previousTimestamp) +" fps");
        previousTimestamp = System.currentTimeMillis();
    }

    /**
     * Opens a Swing window on screen.
     */
    private static void openJavaWindow()
    {
        mScreenProcessed = new VideoImage();
        mScreenSource = new VideoImage();
    }

    /**
     * Forces the swing thread to terminate; I'm sure there is a right
     * way to do this in swing, but this works too.
     */
    private static void closeJavaWindow()
    {
        System.exit(0);
    }
}
