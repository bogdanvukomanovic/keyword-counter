package result_retriever.result;

import java.util.Map;
import java.util.concurrent.Future;

public class WebResult extends Result {

    public WebResult(Future<Map<String, Integer>> result) {
        this.type = ResultType.WEB;
        this.result = result;
    }

    @Override
    public ResultType getType() {
        return this.type;
    }

    @Override
    public Future<Map<String, Integer>> getResult() {
        return this.result;
    }

}
