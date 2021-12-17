
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
public class Discipline implements CloneableItem<Discipline> {
    private Integer codeMatiere;
    private String nomMatiere;
    private Integer codeDiscipline;
    private String nomDiscipline;

    public Discipline() {
        this(0, "", 0, "");
    }
    
    public Discipline(Integer codeMatiere, String nomMatiere, Integer codeDiscipline, String nomDiscipline) {
        this.codeMatiere = codeMatiere;
        this.nomMatiere = nomMatiere;
        this.codeDiscipline = codeDiscipline;
        this.nomDiscipline = nomDiscipline;
    }

    public Discipline(Discipline discipline) {
        this(discipline.codeMatiere,
                discipline.nomMatiere,
                discipline.codeDiscipline,
                discipline.nomDiscipline);
    }

    public Integer getCodeMatiere() {
        return codeMatiere;
    }

    public void setCodeMatiere(Integer codeMatiere) {
        this.codeMatiere = codeMatiere;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public Integer getCodeDiscipline() {
        return codeDiscipline;
    }

    public void setCodeDiscipline(Integer codeDiscipline) {
        this.codeDiscipline = codeDiscipline;
    }

    public String getNomDiscipline() {
        return nomDiscipline;
    }

    public void setNomDiscipline(String nomDiscipline) {
        this.nomDiscipline = nomDiscipline;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.codeMatiere);
        hash = 89 * hash + Objects.hashCode(this.nomMatiere);
        hash = 89 * hash + Objects.hashCode(this.codeDiscipline);
        hash = 89 * hash + Objects.hashCode(this.nomDiscipline);
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
        final Discipline other = (Discipline) obj;
        if (!Objects.equals(this.nomMatiere, other.nomMatiere)) {
            return false;
        }
        if (!Objects.equals(this.nomDiscipline, other.nomDiscipline)) {
            return false;
        }
        if (!Objects.equals(this.codeMatiere, other.codeMatiere)) {
            return false;
        }
        if (!Objects.equals(this.codeDiscipline, other.codeDiscipline)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Discipline{" + "codeMatiere=" + codeMatiere + ", nomMatiere=" + nomMatiere + ", codeDiscipline=" + codeDiscipline + ", nomDiscipline=" + nomDiscipline + '}';
    }

    @Override
    public Discipline cloneItem() {
        return  new Discipline(codeMatiere, nomMatiere, codeDiscipline, nomDiscipline);
    }
    
}
