package result_retriever;

import result_retriever.response.Response;
import result_retriever.response.ResponseStatus;
import result_retriever.result.Result;
import result_retriever.task.FileSummaryTask;
import result_retriever.task.WebDomainTask;
import result_retriever.task.WebSummaryTask;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ResultRetriever {

    private class ResultType {

        final static String FILE = "file";
        final static String WEB = "web";

    }

    ExecutorService threadPool = Executors.newCachedThreadPool();

    private Map<String, Result> results;
    public static Map<String, Optional<Future<Map<String, Integer>>>> webDomainCache = new ConcurrentHashMap<>();
    public static AtomicReference<Future<Map<String, Map<String, Integer>>>> webSummaryCache = new AtomicReference<>();
    public static AtomicReference<Future<Map<String, Result>>> fileSummaryCache = new AtomicReference<>();

    public ResultRetriever(Map<String, Result> results) {
        this.results = results;
        this.fileSummaryCache.set(threadPool.submit(new FileSummaryTask(results))); /* TODO: Write comment */
    }

    public Response getResult(String resultType, String target) {

        Response response;

        switch (resultType) {

            case ResultType.FILE:

                if (!results.containsKey(target)) {
                    return new Response(ResponseStatus.ERROR, "Corpus doesn't exist.", null);
                }

                try {
                    response = new Response(ResponseStatus.OK, "", results.get(target).getResult().get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

                break;

            case ResultType.WEB:

                if (!webDomainCache.containsKey(target)) {
                    return new Response(ResponseStatus.ERROR, "Domain doesn't exist.", null);
                }

                if (webDomainCache.get(target).isEmpty()) {
                    webDomainCache.put(target, Optional.of(threadPool.submit(new WebDomainTask(target, results))));
                }

                try {
                    response = new Response(ResponseStatus.OK, "", webDomainCache.get(target).get().get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

                break;

            default:
                throw new IllegalStateException("Unexpected value: " + resultType);

        }

        return response;
    }

    public Response getSummary(String resultType) {

        Response response;

        switch (resultType) {

            case ResultType.FILE:

                fileSummaryCache.compareAndSet(null, threadPool.submit(new FileSummaryTask(results)));

                try {
                    response = new Response(ResponseStatus.OK, "", fileSummaryCache.get().get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

                break;

            case ResultType.WEB:

                webSummaryCache.compareAndSet(null, threadPool.submit(new WebSummaryTask(results)));

                try {
                    response = new Response(ResponseStatus.OK, "", webSummaryCache.get().get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

                break;

            default:
                throw new IllegalStateException(">> Unexpected value: " + resultType);
        }

        return response;
    }

    public Response queryResult(String resultType, String target) {

        Response response;

        switch (resultType) {

            case ResultType.FILE:

                if (!results.containsKey(target)) {
                    return new Response(ResponseStatus.ERROR, ">> Corpus doesn't exist.", null);
                }

                Result result = results.get(target);

                if (!result.getResult().isDone()) {
                    return new Response(ResponseStatus.IN_PROGRESS, ">> Result is not ready yet.", null);
                } else {

                    try {
                        response = new Response(ResponseStatus.OK, "", result.getResult().get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

                break;

            case ResultType.WEB:

                if (!webDomainCache.containsKey(target)) {
                    return new Response(ResponseStatus.ERROR, ">> Domain doesn't exist.", null);
                }

                if (webDomainCache.get(target).isEmpty()) {
                    webDomainCache.put(target, Optional.of(threadPool.submit(new WebDomainTask(target, results))));
                }

                Future<Map<String, Integer>> r = webDomainCache.get(target).get();

                if (!r.isDone()) {
                    return new Response(ResponseStatus.IN_PROGRESS, ">> Result is not ready yet.", null);
                } else {
                    try {
                        response = new Response(ResponseStatus.OK, "", r.get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }

                break;

            default:
                throw new IllegalStateException(">> Unexpected value: " + resultType);
        }

        return response;
    }

    public Response querySummary(String resultType) {

        Response response;

        switch (resultType) {

            case ResultType.FILE:

                fileSummaryCache.compareAndSet(null, threadPool.submit(new FileSummaryTask(results)));

                if (!fileSummaryCache.get().isDone()) {
                    response = new Response(ResponseStatus.IN_PROGRESS, ">> Result is not ready yet.", null);
                } else {

                    try {
                        response = new Response(ResponseStatus.OK, "", fileSummaryCache.get().get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

                break;

            case ResultType.WEB:

                webSummaryCache.compareAndSet(null, threadPool.submit(new WebSummaryTask(results)));

                if (!webSummaryCache.get().isDone()) {
                    response = new Response(ResponseStatus.IN_PROGRESS, ">> Result is not ready yet.", null);
                } else {

                    try {
                        response = new Response(ResponseStatus.OK, "", webSummaryCache.get().get());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }
                break;

            default:
                throw new IllegalStateException(">> Unexpected value: " + resultType);

        }

        return response;
    }

    public void clearFileSummary() {
        fileSummaryCache.set(null);
    }

    public void clearWebSummary() {
        webSummaryCache.set(null);
    }

    public void stop() {
        threadPool.shutdown();
        System.out.println(">> FINISHED: Result Retriever");
    }

}
