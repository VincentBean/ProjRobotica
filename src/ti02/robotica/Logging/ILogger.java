package ti02.robotica.Logging;

public interface ILogger {

    void Debug(String msg);

    void Info(String msg);

    void Warn(String msg);

    void Error(String msg);

    void Error(Exception ex);

}
