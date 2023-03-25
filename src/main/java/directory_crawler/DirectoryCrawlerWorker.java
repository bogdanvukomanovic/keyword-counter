package directory_crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryCrawlerWorker {

    private final String ROOT = "./src/resources/data/";
    private List<String> directories;

    public DirectoryCrawlerWorker() {
        this.directories = new ArrayList<>();
        this.directories.add("data_0"); this.directories.add("data_1"); this.directories.add("data_2");
    }

    private List<Path> findCorpora() {

        List<Path> corpora = new ArrayList<>();

        for (String directory : directories) {

            try (Stream<Path> stream = Files.walk(Paths.get(ROOT + directory))) {

                corpora.addAll(stream.filter(x -> Files.isDirectory(x) && Paths.get(x.toString()).getFileName().toString().startsWith("corpus_"))
                                     .toList());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return corpora;
    }

    public List<Path> crawl() {
        return findCorpora();
    }

}