package result_retriever;

import result_retriever.response.Response;
import result_retriever.result.Result;
import result_retriever.task.FileSummaryTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ResultRetriever {

    private class ResultType {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    ExecutorService threadPool = Executors.newCachedThreadPool();

    private Map<String, Result> results;
    private Map<String, Boolean> webDomainCache = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> webSummaryCache = new ConcurrentHashMap<>();
    public static AtomicReference<Future<Map<String, Result>>> fileSummaryCache = new AtomicReference<>();

    public ResultRetriever(Map<String, Result> results) {
        this.results = results;
        this.fileSummaryCache.set(threadPool.submit(new FileSummaryTask(results))); /* TODO: Write comment */
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

    public Response getFileSummary() {

        fileSummaryCache.compareAndSet(null, threadPool.submit(new FileSummaryTask(results)));

        try {
            return new Response("OK", "", fileSummaryCache.get().get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public Response queryResult(String resultType, String target) {

        Response response = null;

        switch (resultType) {

            case ResultType.FILE:

                if (!results.containsKey(target)) {
                    return new Response("ERROR", "Corpus doesn't exist.", null);
                }

                Result result = results.get(target);

                if (!result.getResult().isDone()) {
                    return new Response("IN_PROGRESS", "Result is not ready yet.", null);
                } else {

                    try {
                        response = new Response("OK", "", result.getResult().get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

                break;
        }

        return response;
    }

    public Response queryFileSummary() {

        fileSummaryCache.compareAndSet(null, threadPool.submit(new FileSummaryTask(results)));

        if (!fileSummaryCache.get().isDone()) {
            return new Response("IN_PROGRESS", "Result is not ready yet.", null);
        } else {

            try {
                return new Response("OK", "", fileSummaryCache.get().get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public void clearFileSummary() {
        fileSummaryCache.set(null);
    }

    /* TODO: TBD */
//    public Response getFileSummary() {
//        if (fileSummaryCache.compareAndSet(null, threadPool.submit(new FileSummaryTask(results)))) {
//
//            try {
//                response = new Response( "OK", "", fileSummaryCache.get().get());
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//
//        } else {
//
//            try {
//                response = new Response( "OK", "", fileSummaryCache.get().get());
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            } catch (ExecutionException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }

    /* TODO: TBD */
//    public Response getFileSummary() {
//        Response response;
//
//        Future<Map<String, Result>> result = threadPool.submit(new FileSummaryTask(results));
//            try {s
//            response = new Response("OK", "", result.get());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//
//        return response;
//    }

}
