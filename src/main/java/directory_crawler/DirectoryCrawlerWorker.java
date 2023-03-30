package directory_crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DirectoryCrawlerWorker implements Runnable {

    private final String ROOT = "./src/resources/data/";
    private List<String> directories;

    public DirectoryCrawlerWorker() {
        this.directories = new ArrayList<>();
        this.directories.add("data_0"); this.directories.add("data_1"); this.directories.add("data_2");
    }

    private List<Corpus> findCorpora() {

        List<Corpus> corpora = new ArrayList<>();

        for (String directory : directories) {

            try (Stream<Path> stream = Files.walk(Paths.get(ROOT + directory))) {

                corpora.addAll(stream.filter(x -> Files.isDirectory(x) && Paths.get(x.toString()).getFileName().toString().startsWith("corpus_"))
                                     .map(x -> new Corpus(x.getFileName().toString(), x, extractAllTexts(x)))
                                     .toList());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return corpora;
    }

    public List<Text> extractAllTexts(Path path) {

        List<Text> texts = new ArrayList<>();

        for (File file : Objects.requireNonNull(new File(path.toString()).listFiles())) {
            if (!file.isDirectory()) {
                texts.add(new Text(file.getName()));
            }
        }

        return texts;
    }

    public List<Corpus> crawl() {

        List<Corpus> corpora = findCorpora();

        for (Corpus corpus : corpora) {

            System.out.println(corpus.getName());
            System.out.println(corpus.getPath());
            System.out.println(corpus.getTexts());

            for (Text text : corpus.getTexts()) {
                System.out.println("\t" + text.getName());
                System.out.println("\t" + text.getLastModified());
            }

            System.out.println();
        }

        return corpora;
    }

    @Override
    public void run() {

        List<Corpus> corpora = null;   /* TODO: Change to Class Attribute */

        while (true) {

            corpora = findCorpora();

            for (Corpus corpus : corpora) {

                for (Text text : corpus.getTexts()) {

                    File file = new File(corpus.getPath() + "\\" + text.getName());

                    if (Objects.isNull(text.getLastModified()) || file.lastModified() != text.getLastModified()) {
                        /* TODO: Create Job and add it to JobQueue */
                        text.setLastModified(file.lastModified());
                    }

                }
            }

        }

    }

}