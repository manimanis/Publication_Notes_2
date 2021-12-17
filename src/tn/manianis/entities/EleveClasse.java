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
public class EleveClasse implements CloneableItem<EleveClasse> {
    
    private Integer numeroOrdre;
    private String identEleve;
    private String nomPrenom;

    public EleveClasse() {
        this(0, "", "");
    }

    public EleveClasse(Integer numeroOrdre, String identEleve, String nomPrenom) {
        this.numeroOrdre = numeroOrdre;
        this.identEleve = identEleve;
        this.nomPrenom = nomPrenom;
    }

    public Integer getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(Integer numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getIdentEleve() {
        return identEleve;
    }

    public void setIdentEleve(String identEleve) {
        this.identEleve = identEleve;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.numeroOrdre);
        hash = 31 * hash + Objects.hashCode(this.identEleve);
        hash = 31 * hash + Objects.hashCode(this.nomPrenom);
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
        final EleveClasse other = (EleveClasse) obj;
        if (!Objects.equals(this.identEleve, other.identEleve)) {
            return false;
        }
        if (!Objects.equals(this.nomPrenom, other.nomPrenom)) {
            return false;
        }
        if (!Objects.equals(this.numeroOrdre, other.numeroOrdre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EleveClasse{" + "numeroOrdre=" + numeroOrdre + ", identEleve=" + identEleve + ", nomPrenom=" + nomPrenom + '}';
    }

    @Override
    public EleveClasse cloneItem() {
        return new EleveClasse(numeroOrdre, identEleve, nomPrenom);
    }
}
