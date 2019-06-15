package ti02.robotica.Detector;

import jssc.SerialPort;
import jssc.SerialPortException;

public class HardwareController {
    private SerialPort _serial;
    private int _baudrate;
    private final String ACTION_FEED = "n";
    private final String ACTION_RESET = "r";
    private final String ACTION_TEST = "t";
    private final String ACTION_CLOSEALL = "c";
    private final String ACTION_OPENALL = "o";

    public HardwareController(int _baudrate, String _port) {
        this._baudrate = _baudrate;
        _serial = new SerialPort(_port);

    }

    public boolean Connect()
    {
        try
        {
            _serial.openPort();
            _serial.setParams(_baudrate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params

            Thread.sleep(2000); // Give the serial connection time to setup
        }
        catch (SerialPortException e)
        {
            return false;
            // TODO: LOG SERIAL OPEN FAILED
        }
        catch (InterruptedException e)
        {

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
            System.out.print(e);
            System.out.println("LOG SERIAL CLOSE FAILED");
            return false;
            // TODO: LOG SERIAL CLOSE FAILED
        }
        return !_serial.isOpened();
    }

    public void Feed()
    {
        Write(ACTION_FEED);
    }

    public void TestGate()
    {
        Write(ACTION_TEST);
    }

    public void ResetGates()
    {
        Write(ACTION_RESET);
    }

    public void CloseGates()
    {
        Write(ACTION_CLOSEALL);
    }

    public void OpenGates()
    {
        Write(ACTION_OPENALL);
    }

    public void OpenGate(int gate) {
        Write(Integer.toString(gate));
    }

    private void Write(String data)
    {
        if(!_serial.isOpened())
        {
            System.out.println("IK PROBEER TE SCHRIJVEN MAAR IK BEN NIET CONNECTED");
            return;
            // TODO: IK PROBEER TE SCHRIJVEN MAAR IK BEN NIET CONNECTED.
        }

        try {
            _serial.writeBytes(data.getBytes());
        }
        catch (SerialPortException e)
        {
            System.out.println("LOG DAT HET NIET GELUKT IS");
            // TODO: LOG DAT HET NIET GELUKT IS
        }
    }
}
