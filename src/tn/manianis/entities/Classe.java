/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import tn.manianis.interfaces.CloneableItem;

import java.util.Objects;

/**
 *
 * @author manianis
 */
public class Classe implements CloneableItem<Classe> {
    private Integer codeClasse;
    private String nomClasse;

    public Classe() {
        this(0, "");
    }

    
    public Classe(Integer codeClasse, String nomClasse) {
        this.codeClasse = codeClasse;
        this.nomClasse = nomClasse;
    }

    public Integer getCodeClasse() {
        return codeClasse;
    }

    public Integer getCodeNiveau() {
        return codeClasse / 100;
    }

    public void setCodeClasse(Integer codeClasse) {
        this.codeClasse = codeClasse;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.codeClasse);
        hash = 19 * hash + Objects.hashCode(this.nomClasse);
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
        final Classe other = (Classe) obj;
        if (!Objects.equals(this.nomClasse, other.nomClasse)) {
            return false;
        }
        if (!Objects.equals(this.codeClasse, other.codeClasse)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Classe{" + "codeClasse=" + codeClasse + ", nomClasse=" + nomClasse + '}';
    }

    @Override
    public Classe cloneItem() {
        return new Classe(codeClasse, nomClasse);
    }
    
}
