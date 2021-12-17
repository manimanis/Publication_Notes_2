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
public class PeriodeExamen implements CloneableItem<PeriodeExamen> {

    private Integer codePeriodeExamen;
    private String nomPeriodeExamen;

    public PeriodeExamen() {
        this(0, "");
    }

    public PeriodeExamen(Integer codePeriodeExamen, String nomPeriodeExamen) {
        this.codePeriodeExamen = codePeriodeExamen;
        this.nomPeriodeExamen = nomPeriodeExamen;
    }

    public Integer getCodePeriodeExamen() {
        return codePeriodeExamen;
    }

    public void setCodePeriodeExamen(Integer codePeriodeExamen) {
        this.codePeriodeExamen = codePeriodeExamen;
    }

    public String getNomPeriodeExamen() {
        return nomPeriodeExamen;
    }

    public void setNomPeriodeExamen(String nomPeriodeExamen) {
        this.nomPeriodeExamen = nomPeriodeExamen;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.codePeriodeExamen);
        hash = 37 * hash + Objects.hashCode(this.nomPeriodeExamen);
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
        final PeriodeExamen other = (PeriodeExamen) obj;
        if (!Objects.equals(this.nomPeriodeExamen, other.nomPeriodeExamen)) {
            return false;
        }
        if (!Objects.equals(this.codePeriodeExamen, other.codePeriodeExamen)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PeriodeExamen{" + "codePeriodeExamen=" + codePeriodeExamen + ", nomPeriodeExamen=" + nomPeriodeExamen + '}';
    }

    @Override
    public PeriodeExamen cloneItem() {
        return new PeriodeExamen(codePeriodeExamen, nomPeriodeExamen);
    }

}
