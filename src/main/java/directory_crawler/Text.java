package directory_crawler;

public class Text {

    private String name;
    private long lastModified;

    public Text(String name, long lastModified) {
        this.name = name;
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public long getLastModified() {
        return lastModified;
    }

}
