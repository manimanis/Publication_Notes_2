/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author manianis
 */
public class Groupe {

    private Integer anneeScolaire;
    private Enseignant enseignant;
    private Etablissement etablissement;
    private Discipline discipline;
    private Classe classe;
    private PeriodeExamen periodeExamen;
    private Integer nbreClasses;
    private Integer nbreEleves;
    private EpreuvesCollection epreuves;
    private EleveRowCollection rowCollection;

    public Groupe() {
        Calendar toDay = Calendar.getInstance();
        if (toDay.get(Calendar.MONTH) >= 8) {
            anneeScolaire = toDay.get(Calendar.YEAR);
        } else {
            anneeScolaire = toDay.get(Calendar.YEAR) - 1;
        }
        enseignant = new Enseignant();
        etablissement = new Etablissement();
        discipline = new Discipline();
        classe = new Classe();
        periodeExamen = new PeriodeExamen();
        epreuves = new EpreuvesCollection();
        rowCollection = new EleveRowCollection(epreuves);
    }

    public Integer getAnneeScolaire() {
        return anneeScolaire;
    }
    
    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public PeriodeExamen getPeriodeExamen() {
        return periodeExamen;
    }

    public void setPeriodeExamen(PeriodeExamen periodeExamen) {
        this.periodeExamen = periodeExamen;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Integer getNbreClasses() {
        return nbreClasses;
    }

    public void setNbreClasses(Integer nbreClasses) {
        this.nbreClasses = nbreClasses;
    }

    public Integer getNbreEleves() {
        return nbreEleves;
    }

    public void setNbreEleves(Integer nbreEleves) {
        this.nbreEleves = nbreEleves;
    }

    public EpreuvesCollection getEpreuves() {
        return epreuves;
    }

    public void setEpreuves(EpreuvesCollection epreuves) {
        this.epreuves = epreuves;
        rowCollection.setEpreuves(epreuves);
    }
    
    public Epreuve getEpreuve(int p) {
        return epreuves.get(p);
    }

    public EleveRowCollection getRowCollection() {
        return rowCollection;
    }

    public void setRowCollection(EleveRowCollection rowCollection) {
        this.rowCollection = rowCollection;
        this.rowCollection.setEpreuves(epreuves);
    }

    public EleveRow getEleveRow(int pRow) {
        return rowCollection.get(pRow);
    }
    
    public String getCodeNiveau() {
        return classe.getCodeNiveau().toString();
    }
    
}
