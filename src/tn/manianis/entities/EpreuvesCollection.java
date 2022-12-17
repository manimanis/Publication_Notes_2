/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis.entities;

import java.util.ArrayList;
import tn.manianis.interfaces.CloneableArray;

/**
 *
 * @author manianis
 */
public class EpreuvesCollection extends ArrayList<Epreuve> implements CloneableArray<Epreuve, EpreuvesCollection> {

    public EpreuvesCollection() {

    }

    public int findByIds(int codeMatiere, int codeTypeMatiere, int codeTypeEpreuve, int numEpreuve) {
        for (int i = 0; i < size(); i++) {
            Epreuve ep = get(i);
            if (ep.getDiscipline().getCodeMatiere() == codeMatiere && ep.getCodeTypeMatiere() == codeTypeMatiere && ep.getCodeTypeEpreuve() == codeTypeEpreuve && ep.getNumEpreuve() == numEpreuve) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public EpreuvesCollection cloneArray() {
        EpreuvesCollection epreuves = new EpreuvesCollection();
        for (Epreuve epreuve : this) {
            epreuves.add(new Epreuve(epreuve));
        }
        return epreuves;                
    }
}
