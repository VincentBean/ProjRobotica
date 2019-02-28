package ti02.robotica.Logging;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConsoleLogger implements ILogger {

    @Override
    public void Debug(String msg) {
        write(msg);
    }

    @Override
    public void Info(String msg) {
        write(msg);
    }

    @Override
    public void Warn(String msg) {
        write(msg);
    }

    @Override
    public void Error(String msg) {
        write(msg);
    }

    @Override
    public void Error(Exception ex) {
        write("Exception: " + ex.getMessage());
        write("Stacktrace: \n" + ex.getStackTrace().toString());
    }

    private void write(String msg) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

        String calling =  Thread.currentThread().getStackTrace()[2].getMethodName().toUpperCase();

        System.out.println("[" + sdf.format(cal.getTime()) + " " + calling + "] " + msg);
    }
}
