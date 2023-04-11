package result_retriever;

import job_queue.job.ScanType;
import result_retriever.result.Result;
import result_retriever.result.ResultStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResultRetriever {

    private class ResultType {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    private Map<String, Future<Map<String, Integer>>> results;
    private Map<String, Boolean> webDomainCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> webCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> fileCache = new ConcurrentHashMap<>();

    public ResultRetriever(Map<String, Future<Map<String, Integer>>> results) {
        this.results = results;
    }

    public Result getResult(String resultType, String target) {

        Result result = new Result("ERROR", "", null);

        switch (resultType) {

            case ResultType.FILE:

                if (!results.containsKey(target)) {
                    result = new Result("ERROR", "Corpus doesn't exist.", null);
                }

                try {
                    result = new Result("OK", "msg", results.get(target).get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            case ResultType.WEB:
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + resultType);

        }

        return result;
    }

}
