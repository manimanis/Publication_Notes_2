package tn.manianis.utils;

import java.util.Objects;

public class LRUFile {
    private String filename;
    private String title;

    public LRUFile(String filename, String title) {
        this.filename = filename;
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LRUFile)) return false;
        LRUFile lruFile = (LRUFile) o;
        return filename.equals(lruFile.filename) && title.equals(lruFile.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, title);
    }

    @Override
    public String toString() {
        return "LRUFile{" +
                "filename='" + filename + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
