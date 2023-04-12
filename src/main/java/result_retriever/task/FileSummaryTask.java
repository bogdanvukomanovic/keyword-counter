package result_retriever.task;

import result_retriever.result.Result;
import result_retriever.result.ResultType;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileSummaryTask implements Callable<Map<String, Result>> {

    private Map<String, Result> results;

    public FileSummaryTask(Map<String, Result> results) {
        this.results = results;
    }

    @Override
    public Map<String, Result> call() throws Exception {

        /* TODO: Check if this is fine: Before returning assert that all counting jobs are finished */
        Map<String, Result> fileResults = getFileTypeEntries(results);
        for (Map.Entry<String, Result> entry : fileResults.entrySet()) {
            entry.getValue().getResult().get();
        }

        return getFileTypeEntries(fileResults);
    }

//    private boolean isFile(Result result) {
//
//        if (result.getType().equals(ResultType.FILE)) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }

    private <K, V> Map<K, V> getFileTypeEntries(Map<K, V> map) {

        // Predicate<V> predicate = value -> isFile((Result) value);
        Predicate<V> predicate = value ->  ((Result) value).getType().equals(ResultType.FILE);

        return map.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}




