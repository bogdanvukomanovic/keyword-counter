package directory_crawler;

import app.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DirectoryCrawlerWorker implements Runnable {

    private List<String> directories;

    public DirectoryCrawlerWorker() {
        this.directories = new ArrayList<>();
        this.directories.add("data_0"); this.directories.add("data_1"); this.directories.add("data_2");
    }

    private List<Corpus> findCorpora() {

        List<Corpus> corpora = new ArrayList<>();

        for (String directory : directories) {

            try (Stream<Path> stream = Files.walk(Paths.get(Configuration.DATA_ROOT + directory))) {

                corpora.addAll(stream.filter(x -> Files.isDirectory(x) && Paths.get(x.toString()).getFileName().toString().startsWith(Configuration.FILE_CORPUS_PREFIX))
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

    private boolean areFilesModified(Corpus corpus) {

        boolean x = false;

        for (Text text : corpus.getTexts()) {

            File file = new File(corpus.getPath() + "\\" + text.getName());

            /* New file added or any of the recorded files modified */
            if (Objects.isNull(text.getLastModified()) || file.lastModified() != text.getLastModified()) {
                x = true;
                text.setLastModified(file.lastModified());
            }

        }

        return x;
    }

    @Override
    public void run() {

        List<Corpus> candidates = null;   /* TODO: Change to Class Attribute */
        HashMap<Path, Corpus> corpora = new HashMap<Path, Corpus>();

        while (true) {

            candidates = findCorpora();

            for (Corpus corpus : candidates) {

                /* Brand-new corpus */
                if (!corpora.containsKey(corpus.getPath())) {

                    /* TODO: Create Job and add it to JobQueue */
                    System.out.println("Created job for corpus: " + corpus.getName());

                    /* TODO: Set last modified? */
                    for (Text text : corpus.getTexts()) {
                        File file = new File(corpus.getPath() + "\\" + text.getName());
                        text.setLastModified(file.lastModified());
                    }

                    corpora.put(corpus.getPath(), corpus);
                    continue;
                }

                /* Old corpus - check if any files are modified or added in the meantime */
                if (areFilesModified(corpora.get(corpus.getPath()))) {
                    /* TODO: Create Job and add it to JobQueue */
                    System.out.println("Created job for corpus: " + corpus.getName());
                }

            }

        }

    }

}