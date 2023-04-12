package result_retriever.task;

import result_retriever.result.Result;
import result_retriever.result.ResultType;
import result_retriever.result.WebResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebSummaryTask implements Callable<Map<String, Map<String, Integer>>> {

    private Map<String, Result> results;

    public WebSummaryTask(Map<String, Result> results) {
        this.results = results;
    }

    @Override
    public Map<String, Map<String, Integer>> call() throws Exception {

        Map<String, Map<String, Integer>> result = new HashMap<>();
        Map<String, Result> webEntries = getWebTypeEntries(results);

        for (Map.Entry<String, Result> entry : webEntries.entrySet()) {

            String domain = ((WebResult) entry.getValue()).getDomain();

            if (result.containsKey(domain)) {
                result.put(domain, merge(result.get(domain), entry.getValue().getResult().get()));
            } else {
                result.put(domain, entry.getValue().getResult().get());
            }

        }

        return result;
    }

    private Map<String, Integer> merge(Map<String, Integer> m1, Map<String, Integer> m2) {

        return Stream.concat(m1.entrySet().stream(), m2.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
    }

    private <K, V> Map<K, V> getWebTypeEntries(Map<K, V> map) {

        Predicate<V> predicate = value -> ((Result) value).getType().equals(ResultType.WEB);

        return map.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
