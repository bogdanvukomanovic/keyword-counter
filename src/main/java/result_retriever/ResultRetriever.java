package result_retriever;

import result_retriever.response.Response;
import result_retriever.result.Result;
import result_retriever.task.FileSummaryTask;

import java.util.Map;
import java.util.concurrent.*;

public class ResultRetriever {

    private class ResultType {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    ExecutorService threadPool = Executors.newCachedThreadPool();

    private Map<String, Result> results;
    private Map<String, Boolean> webDomainCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> webSummaryCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> fileSummaryCache = new ConcurrentHashMap<>();

    public ResultRetriever(Map<String, Result> results) {
        this.results = results;
    }

    public Response getResult(String resultType, String target) {

        Response response = new Response("ERROR", "", null);

        switch (resultType) {

            case ResultType.FILE:

                if (!results.containsKey(target)) {
                    return new Response("ERROR", "Corpus doesn't exist.", null);
                }

                try {
                    response = new Response("OK", "", results.get(target).getResult().get());
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

        return response;
    }

    public Response getSummary(String resultType) {

        Response response = new Response("ERROR", "", null);

        switch (resultType) {

            case ResultType.FILE:
                Future<Map<String, Result>> result = threadPool.submit(new FileSummaryTask(results));
                try {
                    response = new Response("OK", "", result.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                break;

            case ResultType.WEB:
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + resultType);

        }

        return response;
    }

    /* TODO: Instead of adding Results in FileScanner with "results.put()" so we can update the fileSummaryCache */
    public void addCorpusResult() {}

}
