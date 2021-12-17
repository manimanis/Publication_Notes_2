package tn.manianis.entities;

import java.util.*;
import tn.manianis.interfaces.CloneableArray;

public class ObservationCollection implements List<Observation>, CloneableArray<Observation, ObservationCollection> {

    private ArrayList<Observation> observations;

    public static ObservationCollection fillDefaultValues() {
        ObservationCollection collection = new ObservationCollection();
        for (int i = 0; i <= 20; i++) {
            String obser = "";
            if (i >= 18) {
                obser = "ممتاز";
            } else if (i >= 16) {
                obser = "حسن جدا";
            } else if (i >= 14) {
                obser = "حسن";
            } else if (i >= 12) {
                obser = "قريب من الحسن";
            } else if (i >= 10) {
                obser = "متوسط";
            } else if (i >= 8) {
                obser = "دون المتوسط";
            } else if (i >= 4) {
                obser = "ضعيف";
            } else {
                obser = "ضعيف جدا";
            }
            collection.add(new Observation(i, i * 1.0, obser));
        }
        return collection;
    }

    public ObservationCollection() {
        observations = new ArrayList<>();
    }

    public String getObservation(double note) {
        if (!Note.isValid(note)) {
            return "";
        }
        
        for (int i = size() - 1; i >= 0; i--) {
            Observation observation = get(i);
            if (note >= observation.getNote()) {
                return observation.getObservation();
            }
        }
        return "";
    }

    @Override
    public int size() {
        return observations.size();
    }

    @Override
    public boolean isEmpty() {
        return observations.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return observations.contains(o);
    }

    @Override
    public Iterator<Observation> iterator() {
        return observations.iterator();
    }

    @Override
    public Object[] toArray() {
        return observations.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return observations.toArray(ts);
    }

    @Override
    public boolean add(Observation observation) {
        if (observations.isEmpty()) {
            observations.add(observation);
        } else {
            int p = insertPosition(observation.getNote());
            if (p >= 0) {
                if (!observations.get(p).getNote().equals(observation.getNote())) {
                    observations.add(p, observation);
                } else {
                    observations.set(p, observation);
                }
            } else if (p < 0) {
                observations.add(-p, observation);
            }
        }
        return true;
    }

    public boolean add(double note, String observation) {
        return add(new Observation(0, note, observation));
    }

    @Override
    public boolean remove(Object o) {
        return observations.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return observations.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Observation> collection) {
        int counter = 0;
        for (Observation observation : collection) {
            if (add(observation)) {
                counter++;
            }
        }
        return counter == collection.size();
    }

    @Override
    public boolean addAll(int i, Collection<? extends Observation> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return observations.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return observations.retainAll(collection);
    }

    @Override
    public void clear() {
        observations.clear();
    }

    public Observation get(int index) {
        return observations.get(index);
    }

    @Override
    public Observation set(int i, Observation observation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int i, Observation observation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observation remove(int i) {
        return observations.remove(i);
    }

    public int indexOf(Double note) {
        int p = insertPosition(note);
        if (p < 0) {
            return -1;
        }
        return p;
    }

    @Override
    public int indexOf(Object o) {
        if (o instanceof Observation) {
            return indexOf(((Observation) o).getNote());
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    @Override
    public ListIterator<Observation> listIterator() {
        return observations.listIterator();
    }

    @Override
    public ListIterator<Observation> listIterator(int i) {
        return observations.listIterator(i);
    }

    @Override
    public List<Observation> subList(int i, int i1) {
        return observations.subList(i, i1);
    }

    private int insertPosition(double note) {
        int d = 0;
        int f = observations.size() - 1;
        do {
            int mid = (d + f) / 2;
            Observation obs = observations.get(mid);
            int cmp = obs.getNote().compareTo(note);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                d = mid + 1;
            } else {
                f = mid - 1;
            }
        } while (f >= d);
        return -d;
    }

    @Override
    public ObservationCollection cloneArray() {
        ObservationCollection newObservations = new ObservationCollection();
        for (Observation observation : observations) {
            newObservations.add(observation.cloneItem());
        }
        return newObservations;
    }

}
