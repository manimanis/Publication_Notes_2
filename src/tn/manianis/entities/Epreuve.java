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
public class Epreuve implements CloneableItem<Epreuve> {

    private Etablissement etablissement;
    private Discipline discipline;
    private Integer codeTypeMatiere;
    private Integer codeTypeEpreuve;
    private Integer numEpreuve;
    private String nomEpreuve;
    private String abbrNomEpreuve;
    private Note noteEpreuve;
    private Double coefficient;

    public Epreuve() {
        this(null, null, 0, 0, 0, "", "", new Note(), 1.0);
    }

    
    public Epreuve(Etablissement etablissement, Discipline discipline, Integer codeTypeMatiere, Integer codeTypeEpreuve, Integer numEpreuve, String nomEpreuve, String abbrNomEpreuve, Note noteEpreuve, Double coefficient) {
        this.etablissement = etablissement;
        this.discipline = discipline;
        this.codeTypeMatiere = codeTypeMatiere;
        this.codeTypeEpreuve = codeTypeEpreuve;
        this.numEpreuve = numEpreuve;
        this.nomEpreuve = nomEpreuve;
        this.abbrNomEpreuve = abbrNomEpreuve;
        this.noteEpreuve = noteEpreuve;
        this.coefficient = coefficient;
    }

    public Epreuve(Epreuve epreuve) {
        this(new Etablissement(epreuve.etablissement),
                new Discipline(epreuve.discipline),
                epreuve.codeTypeMatiere,
                epreuve.codeTypeEpreuve,
                epreuve.numEpreuve,
                epreuve.nomEpreuve,
                epreuve.abbrNomEpreuve,
                new Note(epreuve.noteEpreuve),
                epreuve.coefficient);
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Integer getCodeTypeMatiere() {
        return codeTypeMatiere;
    }

    public void setCodeTypeMatiere(int codeTypeMatiere) {
        this.codeTypeMatiere = codeTypeMatiere;
    }

    public Integer getCodeTypeEpreuve() {
        return codeTypeEpreuve;
    }

    public void setCodeTypeEpreuve(int codeTypeEpreuve) {
        this.codeTypeEpreuve = codeTypeEpreuve;
    }

    public Integer getNumEpreuve() {
        return numEpreuve;
    }

    public void setNumEpreuve(int numEpreuve) {
        this.numEpreuve = numEpreuve;
    }

    public String getNomEpreuve() {
        return nomEpreuve;
    }

    public void setNomEpreuve(String nomEpreuve) {
        this.nomEpreuve = nomEpreuve;
    }

    public String getAbbrNomEpreuve() {
        return abbrNomEpreuve;
    }

    public void setAbbrNomEpreuve(String abbrNomEpreuve) {
        this.abbrNomEpreuve = abbrNomEpreuve;
    }

    public Note getNoteEpreuve() {
        return noteEpreuve;
    }

    public void setNoteEpreuve(Note noteEpreuve) {
        this.noteEpreuve = noteEpreuve;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.etablissement);
        hash = 97 * hash + Objects.hashCode(this.discipline);
        hash = 97 * hash + this.codeTypeMatiere;
        hash = 97 * hash + this.codeTypeEpreuve;
        hash = 97 * hash + this.numEpreuve;
        hash = 97 * hash + Objects.hashCode(this.nomEpreuve);
        hash = 97 * hash + Objects.hashCode(this.abbrNomEpreuve);
        hash = 97 * hash + Objects.hashCode(this.noteEpreuve);
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
        final Epreuve other = (Epreuve) obj;
        if (this.codeTypeMatiere != other.codeTypeMatiere) {
            return false;
        }
        if (this.codeTypeEpreuve != other.codeTypeEpreuve) {
            return false;
        }
        if (this.numEpreuve != other.numEpreuve) {
            return false;
        }
        if (!Objects.equals(this.nomEpreuve, other.nomEpreuve)) {
            return false;
        }
        if (!Objects.equals(this.abbrNomEpreuve, other.abbrNomEpreuve)) {
            return false;
        }
        if (!Objects.equals(this.etablissement, other.etablissement)) {
            return false;
        }
        if (!Objects.equals(this.discipline, other.discipline)) {
            return false;
        }
        if (!Objects.equals(this.noteEpreuve, other.noteEpreuve)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Epreuve{" + "etablissement=" + etablissement + ", discipline=" + discipline + ", codeTypeMatiere=" + codeTypeMatiere + ", codeTypeEpreuve=" + codeTypeEpreuve + ", numEpreuve=" + numEpreuve + ", nomEpreuve=" + nomEpreuve + ", abbrNomEpreuve=" + abbrNomEpreuve + ", noteEpreuve=" + noteEpreuve + '}';
    }

    @Override
    public Epreuve cloneItem() {
        return new Epreuve(etablissement.cloneItem(), 
                discipline.cloneItem(), 
                codeTypeMatiere, 
                codeTypeEpreuve, 
                numEpreuve, 
                nomEpreuve, 
                abbrNomEpreuve, 
                noteEpreuve.cloneItem(), 
                coefficient);
    }

}
