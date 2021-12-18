package tn.manianis;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import tn.manianis.entities.entities.secondary.XmlFileEntry;
import tn.manianis.entities.entities.secondary.XmlFileEntryCollection;

/**
 *
 * @author manianis
 */
public class XmlFilesFinder {

    private static final int MAX_FILES = 1024;

    private static FileFilter xmlFilesFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return (pathname.isDirectory()
                    || pathname.getAbsolutePath().toLowerCase().endsWith(".xml"));
        }
    };

    public static XmlFileEntryCollection findXmlFiles(String folderPath) {
        File fPath = new File(folderPath);
        XmlFileEntryCollection xmlFiles = new XmlFileEntryCollection();
        browseForFiles(xmlFiles, fPath);
        return xmlFiles;
    }

    private static void browseForFiles(XmlFileEntryCollection xmlFiles, File folderPath) {
        if (xmlFiles.size() < MAX_FILES) {
            File[] foldersFiles = folderPath.listFiles(xmlFilesFilter);
            for (File file : foldersFiles) {
                if (file.isDirectory()) {
                    browseForFiles(xmlFiles, file);
                } else if (xmlFiles.size() < MAX_FILES) {
                    xmlFiles.add(new XmlFileEntry(file));
                }
            }
        }
    }
}
