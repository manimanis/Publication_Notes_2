package tn.manianis.entities;

import java.util.Objects;
import tn.manianis.interfaces.CloneableItem;

public class Observation implements CloneableItem<Observation> {

    private Integer codeObservation;
    private Double note;
    private String observation;

    public Observation() {
        this(0, 0.0, "");
    }

    public Observation(Integer codeObservation, Double note, String observation) {
        this.codeObservation = codeObservation;
        this.note = note;
        this.observation = observation;
    }

    public Observation(Observation observation) {
        this(observation.codeObservation, observation.note, observation.observation);
    }

    public Integer getCodeObservation() {
        return codeObservation;
    }

    public void setCodeObservation(Integer codeObservation) {
        this.codeObservation = codeObservation;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Observation)) {
            return false;
        }
        Observation that = (Observation) o;
        return codeObservation.equals(that.codeObservation) && note.equals(that.note) && observation.equals(that.observation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeObservation, note, observation);
    }

    @Override
    public String toString() {
        return "Observation{"
                + "codeObservation=" + codeObservation
                + ", note=" + note
                + ", observation='" + observation + '\''
                + '}';
    }

    @Override
    public Observation cloneItem() {
        return new Observation(codeObservation, note, observation);
    }
}
