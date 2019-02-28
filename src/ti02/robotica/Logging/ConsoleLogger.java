package ti02.robotica.Logging;

public class ConsoleLogger implements ILogger {

    @Override
    public void Debug(String msg) {
        System.out.println("[DEBUG] " + msg);
    }

    @Override
    public void Info(String msg) {
        System.out.println("[INFO] " + msg);
    }

    @Override
    public void Warn(String msg) {
        System.out.println("[WARN] " + msg);
    }

    @Override
    public void Error(String msg) {
        System.out.println("[ERROR] " + msg);
    }

    @Override
    public void Error(Exception ex) {
        System.out.println("[ERROR] Exception: " + ex.getMessage());
        System.out.println("Stacktrace: " + ex.getStackTrace().toString());
    }
}
