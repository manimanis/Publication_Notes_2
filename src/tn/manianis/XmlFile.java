/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.manianis;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tn.manianis.entities.*;

/**
 * @author manianis
 */
public class XmlFile {
    
    public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        
        transformer.transform(new DOMSource(doc),
                new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }
    
    public static void addElement(Document doc, Node node, String elem, String value) {
        Element el = doc.createElement(elem);
        el.setTextContent(value);
        node.appendChild(el);
    }
    
    public static boolean saveFile(Groupe groupe, String filename) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            
            buildXmlDocument(doc, groupe);
            
            printDocument(doc, new FileOutputStream(filename));
            return true;
        } catch (ParserConfigurationException | IOException | TransformerException ex) {
            Logger.getLogger(XmlFile.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private static void buildXmlDocument(Document doc, Groupe groupe) {
        // root element
        Element root = doc.createElement("notelev_xml");
        doc.appendChild(root);
        
        buildXmlHeader(doc, root, groupe);
        buildXmlTypesEpreuves(doc, root, groupe.getEpreuves());
        buildXmlNotesEleves(doc, root, groupe);
    }
    
    private static void buildXmlHeader(Document doc, Element root, Groupe groupe) {
        addElement(doc, root, "iuense", groupe.getEnseignant().getIdenUnique());
        addElement(doc, root, "libens", groupe.getEnseignant().getNomEnseignant());
        addElement(doc, root, "codeeetab", groupe.getEtablissement().getCodeEtablissement().toString());
        addElement(doc, root, "libeetab", groupe.getEtablissement().getNomEtablissement());
        addElement(doc, root, "codeperiodexam", groupe.getPeriodeExamen().getCodePeriodeExamen().toString());
        addElement(doc, root, "libperiodexam", groupe.getPeriodeExamen().getNomPeriodeExamen());
        addElement(doc, root, "codeclass", groupe.getClasse().getCodeClasse().toString());
        addElement(doc, root, "libeclass", groupe.getClasse().getNomClasse());
        addElement(doc, root, "codematiere", groupe.getDiscipline().getCodeMatiere().toString());
        addElement(doc, root, "libematier", groupe.getDiscipline().getNomMatiere());
        addElement(doc, root, "nbrclass", groupe.getNbreClasses().toString());
        addElement(doc, root, "codedre", groupe.getEtablissement().getDre().getCodeDRE().toString());
        addElement(doc, root, "drear", groupe.getEtablissement().getDre().getNomDRE());
        addElement(doc, root, "nbrEleve", groupe.getNbreEleves().toString());
        addElement(doc, root, "codedisc", groupe.getDiscipline().getCodeDiscipline().toString());
        addElement(doc, root, "libedisc", groupe.getDiscipline().getNomDiscipline());
    }
    
    private static void buildXmlTypesEpreuves(Document doc, Element root, EpreuvesCollection epreuves) {
        for (Epreuve epreuve : epreuves) {
            Element typeepr = doc.createElement("typeepr");
            root.appendChild(typeepr);
            
            addElement(doc, typeepr, "CODEMATI", epreuve.getDiscipline().getCodeMatiere().toString());
            addElement(doc, typeepr, "CODETYPEMATI", epreuve.getCodeTypeMatiere().toString());
            addElement(doc, typeepr, "CODETYPEEPRE", epreuve.getCodeTypeEpreuve().toString());
            addElement(doc, typeepr, "NUMEEPRE", epreuve.getNumEpreuve().toString());
            addElement(doc, typeepr, "NOTEEPRE", epreuve.getNoteEpreuve().toString());
            addElement(doc, typeepr, "CODEETAB", epreuve.getEtablissement().getCodeEtablissement().toString());
            addElement(doc, typeepr, "libTypeEpr", epreuve.getNomEpreuve());
            addElement(doc, typeepr, "libeMat", epreuve.getDiscipline().getNomMatiere());
            addElement(doc, typeepr, "abretypeeprear", epreuve.getAbbrNomEpreuve());
        }
    }
    
    private static void buildXmlNotesEleves(Document doc, Element root, Groupe groupe) {
        for (EleveRow row : groupe.getRowCollection()) {
            int index = 0;
            for (Note note : row.getNotes()) {
                Element noteelev = doc.createElement("noteelev");
                root.appendChild(noteelev);
                Epreuve epreuve = groupe.getEpreuve(index);
                
                addElement(doc, noteelev, "numOrdre", row.getNumOrdre().toString());
                addElement(doc, noteelev, "prenomnom", row.getNomEleve());
                addElement(doc, noteelev, "CODEMATI", epreuve.getDiscipline().getCodeMatiere().toString());
                addElement(doc, noteelev, "CODETYPEMATI", epreuve.getCodeTypeMatiere().toString());
                addElement(doc, noteelev, "CODETYPEEPRE", epreuve.getCodeTypeEpreuve().toString());
                addElement(doc, noteelev, "NUMEEPRE", epreuve.getNumEpreuve().toString());
                addElement(doc, noteelev, "IDENELEV", row.getIdentEleve());
                addElement(doc, noteelev, "NOTEEPRE", note.toString());
                addElement(doc, noteelev, "CODEETAB", groupe.getEtablissement().getCodeEtablissement().toString());
                addElement(doc, noteelev, "libTypeEpr", epreuve.getNomEpreuve());
                addElement(doc, noteelev, "libeMat", epreuve.getDiscipline().getNomMatiere());
                addElement(doc, noteelev, "obseprof", row.getObservation());
                
                index++;
            }
        }
    }
    
    public static Groupe loadFile(String filename) throws Exception {
        //creating a constructor of file class and parsing an XML file
        File file = new File(filename);
        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        Groupe groupe = new Groupe();
        treatHeader(doc, groupe);
        return groupe;
    }
    
    private static boolean treatHeader(Document doc, Groupe groupe) {
        Element elem = doc.getDocumentElement();
        if (!"notelev_xml".equals(elem.getNodeName())) {
            return false;
        }
        NodeList nodes = elem.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            // System.out.println(node.getNodeName());
            switch (node.getNodeName()) {
                case "iuense":
                    groupe.getEnseignant().setIdenUnique(node.getTextContent());
                    break;
                case "libens":
                    groupe.getEnseignant().setNomEnseignant(node.getTextContent());
                    break;
                case "codeeetab":
                    groupe.getEtablissement().setCodeEtablissement(Long.parseLong(node.getTextContent()));
                case "libeetab":
                    groupe.getEtablissement().setNomEtablissement(node.getTextContent());
                    break;
                case "codeperiodexam":
                    groupe.getPeriodeExamen().setCodePeriodeExamen(Integer.parseInt(node.getTextContent()));
                    break;
                case "libperiodexam":
                    groupe.getPeriodeExamen().setNomPeriodeExamen(node.getTextContent());
                    break;
                case "codeclass":
                    groupe.getClasse().setCodeClasse(Integer.parseInt(node.getTextContent()));
                    break;
                case "libeclass":
                    groupe.getClasse().setNomClasse(node.getTextContent());
                    break;
                case "codematiere":
                    groupe.getDiscipline().setCodeMatiere(Integer.parseInt(node.getTextContent()));
                    break;
                case "libematier":
                    groupe.getDiscipline().setNomMatiere(node.getTextContent());
                    break;
                case "nbrclass":
                    groupe.setNbreClasses(Integer.parseInt(node.getTextContent()));
                    break;
                case "codedre":
                    groupe.getEtablissement().getDre().setCodeDRE(Integer.parseInt(node.getTextContent()));
                    break;
                case "drear":
                    groupe.getEtablissement().getDre().setNomDRE(node.getTextContent());
                    break;
                case "nbrEleve":
                    groupe.setNbreEleves(Integer.parseInt(node.getTextContent()));
                    break;
                case "codedisc":
                    groupe.getDiscipline().setCodeDiscipline(Integer.parseInt(node.getTextContent()));
                    break;
                case "libedisc":
                    groupe.getDiscipline().setNomDiscipline(node.getTextContent());
                    break;
                case "typeepr":
                    groupe.getEpreuves().add(treatTypeEpreuve(groupe, node));
                    break;
                case "noteelev":
                    HashMap<String, String> hm = treatEleveRow(node);
                    EleveRowCollection eleves = groupe.getRowCollection();
                    EleveRow eleveRow = null;
                    int pRow = eleves.findByIdentEleve(hm.get("IDENELEV"));
                    if (pRow == -1) {
                        eleveRow = new EleveRow(groupe.getEpreuves().size());
                        groupe.getRowCollection().add(eleveRow);
                        eleveRow.setIdentEleve(hm.get("IDENELEV"));
                        eleveRow.setNomEleve(hm.get("prenomnom"));
                        eleveRow.setNumOrdre(Integer.parseInt(hm.get("numOrdre")));
                        eleveRow.setObservation(hm.get("obseprof"));
                        pRow = eleves.size() - 1;
                    } else {
                        eleveRow = groupe.getEleveRow(pRow);
                    }
                    int pIndex = groupe.getEpreuves().findByIds(Integer.parseInt(hm.get("CODEMATI")), Integer.parseInt(hm.get("CODETYPEMATI")), Integer.parseInt(hm.get("CODETYPEEPRE")));
                    groupe.getRowCollection().setNote(pRow, pIndex, new Note(hm.get("NOTEEPRE")));
                    break;
            }
        }
//        int newColCount = 6;
//        int colCount = groupe.getEpreuves().size();
//        for (int i = 0; i < groupe.getRowCollection().size(); i++) {
//            EleveRow eleveRow = groupe.getEleveRow(i);
//            Note[] notes = new Note[newColCount];
//            for (int j = 0; j < newColCount; j++) {
//                notes[j] = eleveRow.getNote(j % colCount);
//            }
//            eleveRow.setNotes(notes);
//        }
//        EpreuvesCollection epreuvesCollection = new EpreuvesCollection();
//        for (int j = 0; j < newColCount; j++) {
//            epreuvesCollection.add(groupe.getEpreuve(j % colCount));
//        }
//        groupe.setEpreuves(epreuvesCollection);
        groupe.getRowCollection().sort();
        groupe.getRowCollection().recalcAverages();
        return true;
    }
    
    private static Epreuve treatTypeEpreuve(Groupe groupe, Node node) {
        Epreuve epreuve = new Epreuve();
        epreuve.setEtablissement(groupe.getEtablissement());
        epreuve.setDiscipline(groupe.getDiscipline());
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node chNode = nodes.item(i);
            if (chNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            // System.out.println("- " + chNode.getNodeName());
            switch (chNode.getNodeName()) {
                case "CODETYPEMATI":
                    epreuve.setCodeTypeMatiere(Integer.parseInt(chNode.getTextContent()));
                    break;
                case "CODETYPEEPRE":
                    epreuve.setCodeTypeEpreuve(Integer.parseInt(chNode.getTextContent()));
                    break;
                case "NUMEEPRE":
                    epreuve.setNumEpreuve(Integer.parseInt(chNode.getTextContent()));
                    break;
                case "NOTEEPRE":
                    epreuve.setNoteEpreuve(new Note(chNode.getTextContent()));
                    break;
                case "libTypeEpr":
                    epreuve.setNomEpreuve(chNode.getTextContent());
                    break;
                case "abretypeeprear":
                    epreuve.setAbbrNomEpreuve(chNode.getTextContent());
                    break;
            }
        }
        return epreuve;
    }
    
    private static HashMap<String, String> treatEleveRow(Node node) {
        HashMap<String, String> hm = new HashMap<>();
        // EleveRow eleveRow = new EleveRow(groupe.getEpreuves().size());
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node chNode = nodes.item(i);
            if (chNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            hm.put(chNode.getNodeName(), chNode.getTextContent());
            // System.out.println("- " + chNode.getNodeName());
        }
        return hm;
    }
}
