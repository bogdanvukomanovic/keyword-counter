package result_retriever.result;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class Result {

    protected ResultType type;
    protected Future<Map<String, Integer>> result;

    public abstract ResultType getType();
    public abstract Future<Map<String, Integer>> getResult();

    @Override
    public String toString() {

        try {
            return result.get().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}
