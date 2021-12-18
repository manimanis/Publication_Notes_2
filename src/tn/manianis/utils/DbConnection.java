package tn.manianis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import tn.manianis.entities.*;

import java.sql.*;

public class DbConnection {

    private Connection dbConnection = null;

    public Connection open() throws SQLException, FileNotFoundException {
        if (dbConnection == null) {
            String currentFolder = System.getProperty("user.dir");
            System.out.println("Running in folder : " + currentFolder);
            //String dbFilePath = String.format("%s/testnotes", currentFolder);
            String dbFilePath = String.format("%s/testnotes-v2", currentFolder);
            System.out.println("Searching for DB: " + dbFilePath);
            File dbFile = new File(dbFilePath + ".mv.db");
            if (dbFile.exists() && dbFile.isFile()) {
                System.out.println("DB found!");
                String connectionString = String.format("jdbc:h2:tcp://localhost/%s", dbFilePath);
                dbConnection = DriverManager.getConnection(connectionString, "sa", "");
            } else {
                throw new FileNotFoundException(String.format("No %s DB file found in %s", dbFilePath, currentFolder));
            }
        }
        return dbConnection;
    }

    public void close() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
                dbConnection = null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int updateCoefficients(int codeNiveau, int codePeriodeExam, EpreuvesCollection epreuves) {
        String query = "UPDATE EPREUVE\n"
                + "SET COEFEPRE = ?\n"
                + "WHERE CODENIVE=? AND CODEMATI=? AND CODETYPEMATI=? AND CODEPERIEXAM=? AND CODETYPEEPRE=? AND NUMEEPRE=?";
        int c = 0;
        for (Epreuve epreuve : epreuves) {
            try {
                PreparedStatement stmt = dbConnection.prepareStatement(query);
                stmt.setDouble(1, epreuve.getCoefficient());
                stmt.setString(2, String.format("%06d", codeNiveau));
                stmt.setString(3, String.format("%04d", epreuve.getDiscipline().getCodeMatiere()));
                stmt.setString(4, String.format("%02d", epreuve.getCodeTypeMatiere()));
                stmt.setString(5, String.format("%02d", codePeriodeExam));
                stmt.setString(6, String.format("%02d", epreuve.getCodeTypeEpreuve()));
                stmt.setInt(7, epreuve.getNumEpreuve());
                c += stmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return c;
    }

    public EpreuvesCollection fetchCoefficients(int codeMatiere, int codeNiveau, int codePeriodeExam) {
        String query = "SELECT E.CODEMATI, M.LIBEMATIAR, D.CODEDISC, D.LIBEDISCAR, CODETYPEMATI, E.CODETYPEEPRE, NUMEEPRE, LIBETYPEEPREAR, ABRETYPEEPREAR, COEFEPRE\n"
                + "FROM\n"
                + "  EPREUVE AS E\n"
                + "    INNER JOIN TYPEEPRE AS TE ON E.CODETYPEEPRE=TE.CODETYPEEPRE\n"
                + "    INNER JOIN MATIERE AS M ON E.CODEMATI=M.CODEMATI\n"
                + "    INNER JOIN DISCIPLINE AS D ON M.CODEDISC=D.CODEDISC\n"
                + "WHERE E.CODEMATI=? AND CODENIVE=? AND CODEPERIEXAM=?;";
        EpreuvesCollection epreuvesCollection = new EpreuvesCollection();
        try {
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setString(1, String.format("%04d", codeMatiere));
            statement.setString(2, String.format("%02d", codeNiveau));
            statement.setString(3, String.format("%02d", codePeriodeExam));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Epreuve epreuve = new Epreuve();
                epreuve.setNomEpreuve(resultSet.getString("LIBETYPEEPREAR"));
                epreuve.setAbbrNomEpreuve(resultSet.getString("ABRETYPEEPREAR"));
                epreuve.setCoefficient(resultSet.getDouble("COEFEPRE"));
                epreuve.setNumEpreuve(resultSet.getInt("NUMEEPRE"));
                epreuve.setCodeTypeEpreuve(resultSet.getInt("CODETYPEEPRE"));
                epreuve.setCodeTypeMatiere(resultSet.getInt("CODETYPEMATI"));
                Discipline discipline = new Discipline();
                discipline.setCodeDiscipline(resultSet.getInt("CODEDISC"));
                discipline.setNomDiscipline(resultSet.getString("LIBEDISCAR"));
                discipline.setCodeMatiere(resultSet.getInt("CODEMATI"));
                discipline.setNomMatiere(resultSet.getString("LIBEMATIAR"));
                epreuve.setDiscipline(discipline);
                epreuvesCollection.add(epreuve);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return epreuvesCollection;
    }

    public int deleteObservations() {
        String query = "TRUNCATE TABLE observations;";
        try {
            Statement stmt = dbConnection.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public int insertObservations(ObservationCollection observations) {
        String query = "INSERT INTO observations (NOTE, LIBEOBSE) VALUES (?, ?)";
        int index = 0;;
        int count = 0;
        for (Observation observation : observations) {
            try {
                PreparedStatement statement = dbConnection.prepareStatement(query);
                statement.setDouble(1, observation.getNote());
                statement.setString(2, observation.getObservation());
                count += statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return -1;
            }
        }
        return 0;
    }

    public ObservationCollection fetchObservations() {
        String query = "SELECT * FROM observations ORDER BY note ASC;";
        ObservationCollection observations = new ObservationCollection();
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                Observation observation = new Observation();
                observation.setCodeObservation(resultSet.getInt("CODEOBSE"));
                observation.setNote(resultSet.getDouble("NOTE"));
                observation.setObservation(resultSet.getString("LIBEOBSE"));
                observations.add(observation);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return observations;
    }
}
