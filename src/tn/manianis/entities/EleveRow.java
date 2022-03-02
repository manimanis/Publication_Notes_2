/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import java.util.Arrays;
import tn.manianis.interfaces.CloneableItem;

/**
 *
 * @author manianis
 */
public class EleveRow implements CloneableItem<EleveRow> {
    private Integer numOrdre;
    private String identEleve;
    private String nomEleve;
    private Note[] notes;
    private Note moyenne;
    private String observation;
	private String prenomTuteur;

    public EleveRow(int notesCount) {
        notes = new Note[notesCount];
    }

    public EleveRow(Integer numOrdre, String identEleve, String nomEleve, Note[] notes, Note moyenne, String observation) {
        this.numOrdre = numOrdre;
        this.identEleve = identEleve;
        this.nomEleve = nomEleve;
        this.notes = Arrays.copyOf(notes, notes.length);
        this.moyenne = moyenne;
        this.observation = observation;
    }

    public Integer getNumOrdre() {
        return numOrdre;
    }

    public void setNumOrdre(Integer numOrdre) {
        this.numOrdre = numOrdre;
    }

    public String getIdentEleve() {
        return identEleve;
    }

    public void setIdentEleve(String identEleve) {
        this.identEleve = identEleve;
    }

    public String getNomEleve() {
        return nomEleve;
    }

    public void setNomEleve(String nomEleve) {
        this.nomEleve = nomEleve.replaceAll("ـ", "");
    }
    
    public String getPrenomTuteur() {
		return prenomTuteur;
	}

	public void setPrenomTuteur(String prenom) {
		this.prenomTuteur = prenom;
	}
	
	public String getNomEleveComplet() {
		return String.format("%s (%s)", nomEleve, prenomTuteur);
	}
    
    public int getNotesCount() {
        return notes.length;
    }   

    public Note getNote0() {
        return getNote(0);
    }    
    
    public Note getNote1() {
        return getNote(1);
    }
    
    public Note getNote2() {
        return getNote(2);
    }
    
    public Note getNote3() {
        return getNote(3);
    }
    
    public Note getNote4() {
        return getNote(4);
    }
    
    public Note getNote5() {
        return getNote(5);
    }
    
    public Note getNote(int index) {
        if (index < 0 || index >= notes.length) {
            return null;
        }
        return notes[index];
    }
    
    public void setNote(int index, Note note) {
        notes[index] = note;
    }

    public Note[] getNotes() {
        return notes;
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
    }

    public Note getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Note moyenne) {
        this.moyenne = moyenne;
    }
    
    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        if (observation == null) {
            observation = "";
        }
        this.observation = observation;
    }
    
    public Object[] getAsArray(){
        int colCount = 5 + notes.length;
        Object[] values = new Object[colCount];
        values[0] = getNumOrdre();
        values[1] = getIdentEleve();
        values[2] = getNomEleve();
        values[colCount - 2] = getMoyenne();
        values[colCount - 1] = getObservation();
        System.arraycopy(notes, 0, values, 3, notes.length);
        return values;
    }

    @Override
    public EleveRow cloneItem() {
        return new EleveRow(numOrdre, identEleve, nomEleve, notes, moyenne.cloneItem(), observation);
    }
    
    
}
