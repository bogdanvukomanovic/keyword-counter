package result_retriever;

import result_retriever.response.Response;
import result_retriever.result.Result;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResultRetriever {

    private class ResultType {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    private Map<String, Result> results;
    private Map<String, Boolean> webDomainCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> webCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> fileCache = new ConcurrentHashMap<>();

    public ResultRetriever(Map<String, Result> results) {
        this.results = results;
    }

    public Response getResult(String resultType, String target) {

        Response response = new Response("ERROR", "", null);

        switch (resultType) {

            case ResultType.FILE:

                if (!results.containsKey(target)) {
                    response = new Response("ERROR", "Corpus doesn't exist.", null);
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

}
