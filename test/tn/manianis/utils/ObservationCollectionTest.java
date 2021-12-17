package tn.manianis.utils;


class ObservationCollectionTest {
//
//    @org.junit.jupiter.api.Test
//    void size() {
//
//    }
//
//    @org.junit.jupiter.api.Test
//    void put() {
//        ObservationCollection observations = new ObservationCollection();
//        for (int i = 0; i <= 20; i++) {
//            observations.add(i * 1.0, String.format("Observation %03.1f", i * 1.0));
//        }
//        assertEquals(21, observations.size());
//        for (int i = 0; i <= 20; i++) {
//            Observation obs = observations.get(i);
//            assertEquals(i * 1.0, obs.getNote());
//        }
//
//        observations.clear();
//        for (int i = 20; i >= 0; i--) {
//            observations.add(i * 1.0, String.format("Observation %03.1f", i * 1.0));
//        }
//        assertEquals(21, observations.size());
//        for (int i = 0; i <= 20; i++) {
//            Observation obs = observations.get(i);
//            assertEquals(i * 1.0, obs.getNote());
//        }
//
//        Random rand = new Random();
//        ArrayList<Double> values = new ArrayList<>();
//        for (int i = 0; i < 80; i++) {
//            Double v = 0.0;
//            do {
//                v = rand.nextInt(80) * 0.25;
//            } while (values.contains(v));
//            values.add(v);
//        }
//        observations.clear();
//        for (Double dbl : values) {
//            observations.add(dbl, String.format("Observation %05.2f", dbl));
//        }
//        assertEquals(values.size(), observations.size());
//        for (int i = 1; i < observations.size(); i++) {
//            Observation obs1 = observations.get(i);
//            Observation obs2 = observations.get(i-1);
//            assertTrue(obs1.getNote() > obs2.getNote());
//        }
//
//        observations.clear();
//        for (int i = 20; i >= 0; i--) {
//            observations.add((i % 3) * 5.0, String.format("Observation %03.1f", i * 1.0));
//        }
//        assertEquals(3, observations.size());
//        assertEquals("Observation 0.0", observations.get(0).getObservation());
//        assertEquals("Observation 1.0", observations.get(1).getObservation());
//        assertEquals("Observation 2.0", observations.get(2).getObservation());
//
//        observations.clear();
//        for (int i = 0; i <= 20; i++) {
//            observations.add((i % 3) * 5.0, String.format("Observation %03.1f", i * 1.0));
//        }
//        assertEquals(3, observations.size());
//        assertEquals("Observation 18.0", observations.get(0).getObservation());
//        assertEquals("Observation 19.0", observations.get(1).getObservation());
//        assertEquals("Observation 20.0", observations.get(2).getObservation());
//    }
//
//    @org.junit.jupiter.api.Test
//    void indexOf() {
//        ObservationCollection observations = new ObservationCollection();
//        for (int i = 0; i <= 80; i++) {
//            observations.add(0.25 * i, String.format("%05.2f", 0.25*i));
//        }
//        assertEquals(81, observations.size());
//        for (int i = 0; i <= 80; i++) {
//            assertEquals(i, observations.indexOf(i * 0.25));
//        }
//    }
//
//    @org.junit.jupiter.api.Test
//    void getObservation() {
//        ObservationCollection observations = ObservationCollection.fillDefaultValues();
//        assertEquals(21, observations.size());
//
//        for (int  i = 0; i <= 80; i++) {
//            String obser = "";
//            if (i >= 18) {
//                obser = "ممتاز";
//            } else if (i >= 16) {
//                obser = "حسن جدا";
//            } else if (i >= 14) {
//                obser = "حسن";
//            } else if (i >= 12) {
//                obser = "قريب من الحسن";
//            } else if (i >= 10) {
//                obser = "متوسط";
//            } else if (i >= 8) {
//                obser = "دون المتوسط";
//            } else if (i >= 4) {
//                obser = "ضعيف";
//            } else {
//                obser = "ضعيف جدا";
//            }
//            assertEquals(obser, observations.getObservation(i));
//        }
//    }
//
//    @org.junit.jupiter.api.Test
//    void ctor() {
//        ObservationCollection observations = ObservationCollection.fillDefaultValues();
//        assertEquals(21, observations.size());
//
//        ObservationCollection obs2 = observations.cloneArray();
//        assertEquals(21, obs2.size());
//
//        for (int i = 0; i < observations.size(); i++) {
//            assertEquals(observations.get(i), obs2.get(i));
//        }
//    }
}