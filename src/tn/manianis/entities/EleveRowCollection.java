/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import java.util.ArrayList;
import java.util.Comparator;
import org.apache.commons.lang3.time.DateUtils;
import tn.manianis.interfaces.CloneableArray;

/**
 *
 * @author manianis
 */
public class EleveRowCollection extends ArrayList<EleveRow> implements CloneableArray<EleveRow, EleveRowCollection> {

    private EpreuvesCollection epreuves;
    
    public EleveRowCollection(EpreuvesCollection epreuves) {
        setEpreuves(epreuves);
    }
    
    void setEpreuves(EpreuvesCollection epreuves) {
        this.epreuves = epreuves;
        recalcAverages();
    }
    
    public Note[] getNotes(int row) {
        return get(row).getNotes();
    }
    
    public void setNotes(int row, Note[] notes) {
        get(row).setNotes(notes);
        recalcRowAverage(row);
    }
    
    public Note getNote(int row, int index) {
        if (index < 0 || index >= epreuves.size()) {
            return null;
        }
        return get(row).getNote(index);
    }
    
    public void setNote(int row, int index, Note note) {
        get(row).setNote(index, note);
        recalcRowAverage(row);
    }
    
    public void recalcAverages() {
        for (int i = 0; i < size(); i++) {
            recalcRowAverage(i);
        }
    }
    
    private void recalcRowAverage(int row) {
        EleveRow eleveRow = get(row);
        Note[] notes = eleveRow.getNotes();
        double sumMarks = 0.0;
        double sumCoefs = 0.0;
        int index = 0;
        for (Note note : notes) {
            if (note != null && note.isValid()) {
                double coef = epreuves.get(index).getCoefficient();
                sumMarks += note.getNote() * coef;
                sumCoefs += coef;
            }
            index++;
        }
        if (sumCoefs == 0) {
            eleveRow.setMoyenne(new Note());
        }
        eleveRow.setMoyenne(new Note(sumMarks / sumCoefs));
    }
    
    public int findByIdentEleve(String identEleve) {
        for (int i = 0; i < size(); i++) {
            EleveRow er = get(i);
            if (er.getIdentEleve().equals(identEleve)) {
                return i;
            }
        }
        return -1;
    }

    public void sort() {
        super.sort(new Comparator<EleveRow>() {
            @Override
            public int compare(EleveRow e1, EleveRow e2) {
                return e1.getNumOrdre() - e2.getNumOrdre();
            }
        });
    }

    @Override
    public EleveRowCollection cloneArray() {
        EleveRowCollection collection = new EleveRowCollection(epreuves.cloneArray());
        for (EleveRow eleveRow : this) {
            collection.add(eleveRow.cloneItem());
        }
        return collection;
    }

    

}
