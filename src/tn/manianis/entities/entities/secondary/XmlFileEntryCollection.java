package tn.manianis.entities.entities.secondary;

import java.util.ArrayList;
import tn.manianis.interfaces.CloneableArray;

/**
 *
 * @author manianis
 */
public class XmlFileEntryCollection extends ArrayList<XmlFileEntry> implements CloneableArray<XmlFileEntry, XmlFileEntryCollection>{

    @Override
    public XmlFileEntryCollection cloneArray() {
        XmlFileEntryCollection entryCollection = new XmlFileEntryCollection();
        for (XmlFileEntry flEntry : this) {
            entryCollection.add(new XmlFileEntry(flEntry));
        }
        return entryCollection;
    }
    
}
