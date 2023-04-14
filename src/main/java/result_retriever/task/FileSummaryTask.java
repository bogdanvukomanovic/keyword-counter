package result_retriever.task;

import result_retriever.result.Result;
import result_retriever.result.ResultType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileSummaryTask implements Callable<Map<String, Map<String, Integer>>> {

    private Map<String, Result> results;

    public FileSummaryTask(Map<String, Result> results) {
        this.results = results;
    }

    @Override
    public Map<String, Map<String, Integer>> call() throws Exception {

        Map<String, Map<String, Integer>> result = new HashMap<>();
        for (Map.Entry<String, Result> entry : getFileTypeEntries(results).entrySet()) {
            result.put(entry.getKey(), entry.getValue().getResult().get());
        }

        return result;
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




