package directory_crawler;

import java.nio.file.Path;

public class Text {

    /* TODO: Try to make objects of this class immutable */

    private String name;
    private Long lastModified;
    private String path;

    public Text(String name, long lastModified) {
        this.name = name;
        this.lastModified = lastModified;
    }

    public Text(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Text(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

}
