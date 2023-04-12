package result_retriever.result;

import java.util.Map;
import java.util.concurrent.Future;

public class WebResult extends Result {

    private String domain;

    public WebResult(Future<Map<String, Integer>> result, String domain) {
        this.type = ResultType.WEB;
        this.result = result;
        this.domain = domain;
    }

    @Override
    public ResultType getType() {
        return this.type;
    }

    @Override
    public Future<Map<String, Integer>> getResult() {
        return this.result;
    }

    public String getDomain() {
        return domain;
    }

}
