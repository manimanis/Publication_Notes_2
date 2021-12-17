/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import java.util.Objects;
import tn.manianis.interfaces.CloneableItem;

/**
 * @author manianis
 */
public class Etablissement implements CloneableItem<Etablissement> {

    private Long codeEtablissement;
    private String nomEtablissement;
    private DRE dre;

    public Etablissement() {
        this(0L, "", new DRE());
    }

    public Etablissement(Long codeEtablissement, String nomEtablissement) {
        this(codeEtablissement, nomEtablissement, new DRE());
    }

    public Etablissement(Long codeEtablissement, String nomEtablissement, DRE dre) {
        this.codeEtablissement = codeEtablissement;
        this.nomEtablissement = nomEtablissement;
        this.dre = dre;
    }

    public Etablissement(Etablissement etablissement) {
        this(etablissement.codeEtablissement,
                etablissement.nomEtablissement,
                new DRE(etablissement.dre));
    }

    public Long getCodeEtablissement() {
        return codeEtablissement;
    }

    public void setCodeEtablissement(Long codeEtablissement) {
        this.codeEtablissement = codeEtablissement;
    }

    public String getNomEtablissement() {
        return nomEtablissement;
    }

    public void setNomEtablissement(String nomEtablissement) {
        this.nomEtablissement = nomEtablissement;
    }

    public DRE getDre() {
        return dre;
    }

    public void setDre(DRE dre) {
        this.dre = dre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.codeEtablissement);
        hash = 83 * hash + Objects.hashCode(this.nomEtablissement);
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
        final Etablissement other = (Etablissement) obj;
        if (!Objects.equals(this.nomEtablissement, other.nomEtablissement)) {
            return false;
        }
        if (!Objects.equals(this.codeEtablissement, other.codeEtablissement)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Etablissement{" + "codeEtablissement=" + codeEtablissement + ", nomEtablissement=" + nomEtablissement + ", dre=" + dre + '}';
    }

    @Override
    public Etablissement cloneItem() {
        return  new Etablissement(codeEtablissement, nomEtablissement, dre.cloneItem());
    }

}
