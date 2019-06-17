package ti02.robotica.Detector;

import jssc.SerialPort;
import jssc.SerialPortException;
import ti02.robotica.Logging.CurrentLogger;

public class HardwareController {
    private SerialPort _serial;
    private int _baudrate;
    private final String ACTION_FEED = "n";
    private final String ACTION_RESET = "r";
    private final String ACTION_TEST = "t";
    private final String ACTION_CLOSEALL = "c";
    private final String ACTION_OPENALL = "o";
    private final String ACTION_STEP = "s";

    public void setDoStep(Boolean doStep) {
        this.doStep = doStep;
    }

    private Boolean doStep = true;
    private long startMillis; // Keep track of millis to compensate for delay

    public HardwareController(int _baudrate, String _port) {
        this._baudrate = _baudrate;
        _serial = new SerialPort(_port);

        startMillis = System.currentTimeMillis();
    }

    public boolean Connect()
    {
        CurrentLogger.Logger.Info("Connecting..");

        try
        {
            _serial.openPort();
            _serial.setParams(_baudrate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params

            Thread.sleep(2000); // Give the serial connection time to wake up
        }
        catch (SerialPortException e)
        {
            CurrentLogger.Logger.Error(e);
            return false;
        }
        catch (InterruptedException e)
        {
            CurrentLogger.Logger.Error(e);
        }

        return _serial.isOpened();
    }

    public boolean Disconnect()
    {
        try
        {
            _serial.closePort();
        }
        catch (SerialPortException e)
        {
            CurrentLogger.Logger.Error("LOG SERIAL CLOSE FAILED");
            CurrentLogger.Logger.Error(e);
            return false;
        }
        return !_serial.isOpened();
    }

    public void Feed()
    {
        CurrentLogger.Logger.Info("Feeding..");
        Write(ACTION_FEED);
    }

    public void TestGate()
    {
        CurrentLogger.Logger.Info("Testing all gates..");
        Write(ACTION_TEST);
    }

    public void ResetGates()
    {
        CurrentLogger.Logger.Info("Resetting all gates..");
        Write(ACTION_RESET);
    }

    public void CloseGates()
    {
        CurrentLogger.Logger.Info("Closing all gates..");
        Write(ACTION_CLOSEALL);
    }

    public void OpenGates()
    {
        CurrentLogger.Logger.Info("Opening all gates..");
        Write(ACTION_OPENALL);
    }

    public void OpenGate(int gate) {
        CurrentLogger.Logger.Info("Opening gate " + gate + "..");
        Write(Integer.toString(gate));
    }

    public void Step() {
        if (!_serial.isOpened()) {
            CurrentLogger.Logger.Error("IK PROBEER TE SCHRIJVEN MAAR IK BEN NIET CONNECTED");
            return;
        }

        for (int i = 0; i <= 2; i++) { // Do two steps (1.8 degrees)
            try {
                _serial.writeBytes("s".getBytes());
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

    private void Write(String data)
    {
        // Only send a command every 7 seconds to compensate for camera delays
        if (System.currentTimeMillis() > startMillis + 7000) {
            if (!_serial.isOpened()) {
                CurrentLogger.Logger.Error("IK PROBEER TE SCHRIJVEN MAAR IK BEN NIET CONNECTED");
                return;
            }

            try {
                _serial.writeBytes(data.getBytes());
            } catch (SerialPortException e) {
                CurrentLogger.Logger.Error(e);
            }

            startMillis = System.currentTimeMillis();
        }
    }
}
