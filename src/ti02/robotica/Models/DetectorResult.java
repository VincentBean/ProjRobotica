package ti02.robotica.Models;

public class DetectorResult<T> {
    private T Result;

    public DetectorResult(T result) {
        Result = result;
    }

    public T getResult() {
        return Result;
    }
}
