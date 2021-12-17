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
public class Enseignant implements CloneableItem<Enseignant> {
    private String idenUnique;
    private String nomEnseignant;

    public Enseignant() {
        this("", "");
    }

    public Enseignant(String idenUnique, String nomEnseignant) {
        this.idenUnique = idenUnique;
        this.nomEnseignant = nomEnseignant;
    }

    public String getIdenUnique() {
        return idenUnique;
    }

    public void setIdenUnique(String idenUnique) {
        this.idenUnique = idenUnique;
    }

    public String getNomEnseignant() {
        return nomEnseignant;
    }

    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idenUnique);
        hash = 79 * hash + Objects.hashCode(this.nomEnseignant);
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
        final Enseignant other = (Enseignant) obj;
        if (!Objects.equals(this.idenUnique, other.idenUnique)) {
            return false;
        }
        if (!Objects.equals(this.nomEnseignant, other.nomEnseignant)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Enseignant{" + "idenUnique=" + idenUnique + ", nomEnseignant=" + nomEnseignant + '}';
    }

    @Override
    public Enseignant cloneItem() {
        return new Enseignant(idenUnique, nomEnseignant);
    }
    
}
