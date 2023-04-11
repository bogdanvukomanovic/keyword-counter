package result_retriever.result;

import java.util.Map;
import java.util.concurrent.Future;

public abstract class Result {

    protected ResultType type;
    protected Future<Map<String, Integer>> result;

    public abstract ResultType getType();
    public abstract Future<Map<String, Integer>> getResult();

}
