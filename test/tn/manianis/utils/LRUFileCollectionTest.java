package tn.manianis.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LRUFileCollectionTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void capacity() {
    }

    @Test
    public void size() {
        LRUFileCollection fileCollection = new LRUFileCollection();
        Assert.assertEquals(0, fileCollection.size());
        for (int i = 0; i < fileCollection.capacity(); i++) {
            fileCollection.add(new LRUFile(String.format("file%d", i), String.format("title%d", i)));
            Assert.assertEquals(i + 1, fileCollection.size());
        }
        for (int i = 0; i < 2 * fileCollection.capacity(); i++) {
            fileCollection.add(new LRUFile("extra_file", "extra_title"));
            Assert.assertEquals(fileCollection.capacity(), fileCollection.size());
        }
    }

    @Test
    public void add() {
        LRUFileCollection fileCollection = new LRUFileCollection();
        for (int i = 0; i < fileCollection.capacity() * 2; i++) {
            LRUFile lruFile = new LRUFile(String.format("file%d", i), String.format("title%d", i));
            fileCollection.add(lruFile);
            LRUFile lastLRUFile = fileCollection.get(fileCollection.size() - 1);
            Assert.assertEquals(lruFile.getFilename(), lastLRUFile.getFilename());
            Assert.assertEquals(lruFile.getTitle(), lastLRUFile.getTitle());
            if (i >= fileCollection.capacity() - 1) {
                int j = i - fileCollection.capacity() + 1;
                LRUFile fLRUFile = new LRUFile(String.format("file%d", j), String.format("title%d", j));
                LRUFile firstLRUFile = fileCollection.get(0);
                Assert.assertEquals(fLRUFile.getFilename(), firstLRUFile.getFilename());
                Assert.assertEquals(fLRUFile.getTitle(), firstLRUFile.getTitle());
            }
        }
        Assert.assertEquals(fileCollection.capacity(), fileCollection.size());
    }

    @Test
    public void testAdd() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void removeAt() {
    }

    @Test
    public void testRemove() {
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get() {
        LRUFileCollection fileCollection = new LRUFileCollection();
        for (int i = 0; i < fileCollection.capacity() / 2; i++) {
            LRUFile lruFile = new LRUFile(String.format("file%d", i), String.format("title%d", i));
            fileCollection.add(lruFile);
        }
        fileCollection.get(-1);
        fileCollection.get(fileCollection.size());
    }

    @Test
    public void set() {
    }

    @Test
    public void findByFilename() {
    }

    @Test
    public void iterator() {
        LRUFileCollection fileCollection = new LRUFileCollection();
        for (int i = 0; i < fileCollection.capacity() / 2; i++) {
            LRUFile lruFile = new LRUFile(String.format("file%d", i), String.format("title%d", i));
            fileCollection.add(lruFile);
        }
        int i = 0;
        for (LRUFile lruFile : fileCollection) {
            LRUFile lruNewFile = new LRUFile(String.format("file%d", i), String.format("title%d", i));
            Assert.assertEquals(lruNewFile.getFilename(), lruFile.getFilename());
            Assert.assertEquals(lruNewFile.getTitle(), lruFile.getTitle());
            i++;
        }
    }

    @Test
    public void loadLRU() {
    }

    @Test
    public void saveLRU() {
    }
}
