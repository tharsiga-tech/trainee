package dao;

import db.DBConnection;
import model.Trainee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TraineeDao {

    // Save trainee
    public boolean saveTrainee(Trainee trainee) {
        String sql = "INSERT INTO trainee (name, email, department, stipend) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql) ) {

            conn.setAutoCommit(false);

            stmt.setString(1, trainee.getName());
            stmt.setString(2, trainee.getEmail());
            stmt.setString(3, trainee.getDepartment());
            stmt.setDouble(4, trainee.getStipend());

            int rowsInserted = stmt.executeUpdate();
            conn.commit();

            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all trainees
    public List<Trainee> getAllTrainees() {
        List<Trainee> traineeList = new ArrayList<>();
        String sql = "SELECT * FROM trainee";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trainee trainee = new Trainee();
                trainee.setId(rs.getInt("id"));
                trainee.setName(rs.getString("name"));
                trainee.setEmail(rs.getString("email"));
                trainee.setDepartment(rs.getString("department"));
                trainee.setStipend(rs.getDouble("stipend"));

                traineeList.add(trainee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return traineeList;
    }

    //Get trainee by ID
    public Trainee getTraineeById(int id) {
        Trainee trainee = null;
        String sql = "SELECT * FROM trainee WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                trainee = new Trainee();
                trainee.setId(rs.getInt("id"));
                trainee.setName(rs.getString("name"));
                trainee.setEmail(rs.getString("email"));
                trainee.setDepartment(rs.getString("department"));
                trainee.setStipend(rs.getDouble("stipend"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainee;
    }

    // Delete trainee by ID
    public boolean deleteTrainee(int id) {
        String sql = "DELETE FROM trainee WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update trainee by ID
    public boolean updateTrainee(Trainee trainee) {
        String sql = "UPDATE trainee SET name = ?, email = ?, department = ?, stipend = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trainee.getName());
            stmt.setString(2, trainee.getEmail());
            stmt.setString(3, trainee.getDepartment());
            stmt.setDouble(4, trainee.getStipend());
            stmt.setInt(5, trainee.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    // Update trainee by ID localhost path set
//    public boolean updateTrainee(Trainee trainee, int id) {
//        String sql = "UPDATE trainee SET name = ?, email = ?, department = ?, stipend = ? WHERE id = ?";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, trainee.getName());
//            stmt.setString(2, trainee.getEmail());
//            stmt.setString(3, trainee.getDepartment());
//            stmt.setDouble(4, trainee.getStipend());
//            stmt.setInt(5, id);
//
//            int rowsUpdated = stmt.executeUpdate();
//            return rowsUpdated > 0;
//        } catch(SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}