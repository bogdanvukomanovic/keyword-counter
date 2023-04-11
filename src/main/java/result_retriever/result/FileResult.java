package result_retriever.result;

import java.util.Map;
import java.util.concurrent.Future;

public class FileResult extends Result {

    private String corpusName;

    public FileResult(Future<Map<String, Integer>> result, String corpusName) {
        this.type = ResultType.FILE;
        this.result = result;
        this.corpusName = corpusName;
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