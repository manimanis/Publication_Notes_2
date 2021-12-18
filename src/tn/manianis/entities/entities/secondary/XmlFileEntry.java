package tn.manianis.entities.entities.secondary;

import java.io.File;
import java.util.Objects;
import tn.manianis.interfaces.CloneableItem;

/**
 *
 * @author manianis
 */
public class XmlFileEntry implements CloneableItem<XmlFileEntry> {

    Boolean checked;
    String filename;
    String filepath;

    public XmlFileEntry() {
        this(true, "", "");
    }

    public XmlFileEntry(String filepath) {
        this(true, filepath, filepath.substring(filepath.lastIndexOf(File.separator) + 1));
    }

    public XmlFileEntry(Boolean checked, String filepath, String filename) {
        this.checked = checked;
        this.filename = filename;
        this.filepath = filepath;
    }

    public XmlFileEntry(File file) {
        this(true, file.getAbsolutePath(), file.getName());
    }

    public XmlFileEntry(XmlFileEntry otherEntry) {
        this(otherEntry.checked, otherEntry.filepath, otherEntry.filename);
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.filename);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XmlFileEntry other = (XmlFileEntry) obj;
        return Objects.equals(this.filename, other.filename);
    }

    @Override
    public String toString() {
        return "XmlFileEntry{" + "checked=" + checked + ", filename=" + filename + ", filepath=" + filepath + '}';
    }

    @Override
    public XmlFileEntry cloneItem() {
        return new XmlFileEntry(checked, filepath, filename);
    }

}
