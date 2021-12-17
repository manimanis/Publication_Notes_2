package tn.manianis.interfaces;

import java.util.List;

public interface CloneableArray <T extends CloneableItem, CA extends List<T>> extends List<T> {
    CA cloneArray();
}
