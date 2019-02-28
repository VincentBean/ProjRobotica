package ti02.robotica.Logging;

public class ConsoleLogger implements ILogger {

    @Override
    public void Debug(String msg) {
        write("[DEBUG] " + msg);
    }

    @Override
    public void Info(String msg) {
        write("[INFO] " + msg);
    }

    @Override
    public void Warn(String msg) {
        write("[WARN] " + msg);
    }

    @Override
    public void Error(String msg) {
        write("[ERROR] " + msg);
    }

    @Override
    public void Error(Exception ex) {
        write("[ERROR] Exception: " + ex.getMessage());
        write("Stacktrace: \n" + ex.getStackTrace().toString());
    }

    private void write(String msg) {
        System.out.println(msg);
    }
}
