package result_retriever.task;


import java.util.Map;
import java.util.concurrent.Callable;

public class WebAddToDomainCacheTask implements Callable<Map<String, Integer>> {

    /* TODO: Not ideal solution. Class CompletableFuture seems to resolve this situation. */

    private Map<String, Integer> result;

    public WebAddToDomainCacheTask(Map<String, Integer> result) {
        this.result = result;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        return result;
    }

}
