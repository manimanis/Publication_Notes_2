package tn.manianis.utils;

import java.util.Iterator;
import java.util.prefs.Preferences;

public class LRUFileCollection implements Iterable<LRUFile> {
    private int maxSize;
    private int startIndex;
    private int nSize;
    private LRUFile[] items;

    public LRUFileCollection() {
        this(10);
    }

    public LRUFileCollection(int maxSize) {
        this.maxSize = maxSize;
        this.startIndex = 0;
        this.nSize = 0;
        this.items = new LRUFile[maxSize];
    }

    public int capacity() {
        return maxSize;
    }

    public int size() {
        return nSize;
    }

    public boolean isEmpty() {
        return nSize == 0;
    }

    public void add(LRUFile lruFile) {
        int p = findByFilename(lruFile.getFilename());
        if (p != -1) {
            removeAt(p);
        }
        if (nSize == maxSize) {
            items[startIndex] = lruFile;
            startIndex = (startIndex + 1) % maxSize;
        } else {
            items[(startIndex + nSize) % maxSize] = lruFile;
            nSize += 1;
        }
    }

    public void add(String filename, String title) {
        add(new LRUFile(filename, title));
    }

    public void clear() {
        nSize = 0;
    }

    public LRUFile remove() {
        if (nSize > 0) {
            return removeAt(0);
        }
        return null;
    }

    public LRUFile removeAt(int index) {
        if (nSize > 0) {
            LRUFile oldFile = items[(startIndex + index) % maxSize];
            for (int i = index; i < nSize - 1; i++) {
                items[(startIndex + i) % maxSize] = items[(startIndex + i + 1) % maxSize];
            }
            nSize--;
            return oldFile;
        }
        return null;
    }

    public LRUFile remove(String filename) {
        int pos = findByFilename(filename);
        if (pos != -1) {
            return removeAt(pos);
        }
        return null;
    }

    public LRUFile get(int index) {
        if (isEmpty() || index < 0 || index >= nSize) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return items[(startIndex + index) % maxSize];
    }

    public void set(int index, LRUFile lruFile) {
        if (isEmpty() || index < 0 || index >= nSize) {
            throw new ArrayIndexOutOfBoundsException();
        }
        items[(startIndex + index) % maxSize] = lruFile;
    }

    public int findByFilename(String filename) {
        for (int i = 0; i < nSize; i++) {
            if (get(i).getFilename().equalsIgnoreCase(filename)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<LRUFile> iterator() {
        Iterator<LRUFile> it = new Iterator<LRUFile>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < nSize;
            }

            @Override
            public LRUFile next() {
                return get(currIdx++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    public static LRUFileCollection loadLRU() {
        Preferences prefs = Preferences.userRoot().node(LRUFile.class.getName());
        LRUFileCollection lruFiles = new LRUFileCollection();
        int fileCount = prefs.getInt("LRU_FILES_COUNT", 0);
        for (int i = 0; i < fileCount; i++) {
            String filename = prefs.get(String.format("FILE_NAME_%02d", i + 1), "");
            String title = prefs.get(String.format("FILE_TITLE_%02d", i + 1), "");
            lruFiles.add(new LRUFile(filename, title));
        }
        return lruFiles;
    }

    public static void saveLRU(LRUFileCollection lruFiles) {
        Preferences prefs = Preferences.userRoot().node(LRUFile.class.getName());
        prefs.putInt("LRU_FILES_COUNT", lruFiles.size());
        int i = 0;
        for (LRUFile lruFile : lruFiles) {
            prefs.put(String.format("FILE_NAME_%02d", i + 1), lruFile.getFilename());
            prefs.put(String.format("FILE_TITLE_%02d", i + 1), lruFile.getTitle());
            i++;
        }
    }
}
