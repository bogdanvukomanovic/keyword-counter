package directory_crawler;

public class Text {

    /* TODO: Try to make objects of this class immutable */

    private String name;
    private Long lastModified;

    public Text(String name, long lastModified) {
        this.name = name;
        this.lastModified = lastModified;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

}
