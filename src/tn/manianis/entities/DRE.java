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
public class DRE implements CloneableItem<DRE> {
    private Integer codeDRE;
    private String nomDRE;

    public DRE() {
        this(0, "");
    }
    
    public DRE(Integer codeDRE, String nomDRE) {
        this.codeDRE = codeDRE;
        this.nomDRE = nomDRE;
    }

    public DRE(DRE dre) {
        this(dre.codeDRE, dre.nomDRE);
    }

    public Integer getCodeDRE() {
        return codeDRE;
    }

    public void setCodeDRE(Integer codeDRE) {
        this.codeDRE = codeDRE;
    }

    public String getNomDRE() {
        return nomDRE;
    }

    public void setNomDRE(String nomDRE) {
        this.nomDRE = nomDRE;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.codeDRE);
        hash = 83 * hash + Objects.hashCode(this.nomDRE);
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
        final DRE other = (DRE) obj;
        if (!Objects.equals(this.nomDRE, other.nomDRE)) {
            return false;
        }
        if (!Objects.equals(this.codeDRE, other.codeDRE)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DRE{" + "codeDRE=" + codeDRE + ", nomDRE=" + nomDRE + '}';
    }

    @Override
    public DRE cloneItem() {
        return new DRE(codeDRE, nomDRE);
    }
}
