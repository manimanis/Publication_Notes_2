/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import java.util.Objects;
import tn.manianis.interfaces.CloneableItem;

/**
 *
 * @author manianis
 */
public class Note implements CloneableItem<Note>, Comparable<Note> {
    public static final Double NON_AFFECTEE = 55.55;
    public static final Double ABSENT = 88.88;
    public static final Double DISPENSE = 99.99;
    
    private Double note;

    public Note() {
        setNote(NON_AFFECTEE);
    }
    
    public Note(Double note) {
        setNote(note);
    }
    
    public Note(String str) {
        setNote(str);
    }
    
    public Note(Note note) {
        setNote(note.note);
    }

    public Double getNote() {
        return note;
    }

    public final void setNote(Double note) {
        this.note = (note < 100.0) ? note : (note/100.0);
    }
    
    public final void setNote(String note) {
        setNote(parseDouble(note));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.note);
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
        final Note other = (Note) obj;
        if (!Objects.equals(this.note, other.note)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return fromDouble(note);
    }
    
    public static double parseDouble(String str) {
        if (str.equalsIgnoreCase("--.--")) {
            return NON_AFFECTEE;
        }
        if (str.equalsIgnoreCase("غ.ش.")) {
            return ABSENT;
        }
        if (str.equalsIgnoreCase("معفى")) {
            return DISPENSE;
        }
        try {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException e) {
            return NON_AFFECTEE;
        }
    }
    
    public static String fromDouble(double note) {
        if (note == NON_AFFECTEE) {
            return "--.--";
        }
        if (note == ABSENT) {
            return "غ.ش.";
        }
        if (note == DISPENSE) {
            return "معفى";
        }
        if (note >= 0.0 && note <= 20.0) {
            return String.format("%05.2f", note);
        }
        return "--.--";
    }

    public static boolean isValid(double note) {
        return (note >= 0.0 && note <= 20.0);
    }

    public boolean isValid() {
        return isValid(note);
    }

    @Override
    public Note cloneItem() {
        return new Note(note);
    }

    @Override
    public int compareTo(Note t) {
        return note.compareTo(t.note);
    }
}
