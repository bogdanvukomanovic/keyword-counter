package directory_crawler;

import java.nio.file.Path;
import java.util.List;

public class Corpus {

    /* TODO: Try to make objects of this class immutable */

    private final String name;
    private final Path path;
    private List<Text> texts;

    public Corpus(String name, Path path, List<Text> texts) {
        this.name = name;
        this.path = path;
        this.texts = texts;
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }

    public List<Text> getTexts() {
        return texts;
    }

}
