package com.exoapp.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PatientDBOp {

    public Map<LocalDate, Float> getSessions(Long id) throws SQLException {

        String sql_sessions = "SELECT * FROM exoapp.exercise_sessions WHERE user_id = ?";
        Map<LocalDate, Float> sessions = new HashMap<>();
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql_sessions);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            LocalDate date = LocalDate.parse(rs.getString("date"));
            Float duration = rs.getFloat("duration");
            sessions.put(date, duration);
        }
        return sessions;
    }
    public Map<LocalDate, Integer> getPassiveScores(Long id) throws SQLException {
        String sql_sessions = "SELECT * FROM exoapp.passive_mode WHERE user_id = ?";
        Map<LocalDate, Integer> sessions = new HashMap<>();
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql_sessions);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            LocalDate date = LocalDate.parse(rs.getString("date"));
            Integer score = rs.getInt("score");
            System.out.println("FOUND PASSIVE SCORE: " + score);
            sessions.put(date, score);
        }
        return sessions;
    }

    public void addExerciseSession(Long id, LocalDate date, Float duration) throws SQLException {
        Float d = checkSessionDay(id, date);
        if(d != null)
        {
            String sql = "UPDATE exoapp.exercise_sessions SET duration=? WHERE (user_id=?) AND (date=?);";
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, duration+d);
            stmt.setLong(2, id);
            stmt.setString(3, date.toString());
            stmt.executeUpdate();
        }
        else
        {
            String sql = "INSERT INTO exoapp.exercise_sessions VALUES (?,?,?);";
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.setString(2, date.toString());
            stmt.setFloat(3, duration);
            stmt.executeUpdate();
        }

    }

    /**
     * Checks if a exercise session already exists for given user& date
     * @param id
     * @param date
     * @return session duration or null if it doesnt exist
     * @throws SQLException
     */
    public Float checkSessionDay(Long id, LocalDate date) throws SQLException {
        String sql_sessions = "SELECT duration FROM exoapp.exercise_sessions WHERE (user_id = ?) and (date = ?)";
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql_sessions);
        stmt.setLong(1, id);
        stmt.setString(2, date.toString());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            return rs.getFloat("duration");
        }
        else
            return null;
    }

    /**
     * Checks if a passive mode session already exists for given user& date
     * @param id
     * @param date
     * @return passive session score or 0 (if it doesn't exist)
     * @throws SQLException
     */
    public Integer checkPassiveDay(Long id, LocalDate date) throws SQLException {
        String sql_sessions = "SELECT score FROM exoapp.passive_mode WHERE (user_id = ?) and (date = ?)";
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql_sessions);
        stmt.setLong(1, id);
        stmt.setString(2, date.toString());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            return rs.getInt("score");
        }
        else
            return 0;
    }

    public void addPassiveSession(Long userId, LocalDate date, Integer score) throws SQLException {
        int prevScore = checkPassiveDay(userId, date);
        String sql;
        if(prevScore == 0) {
            sql = "INSERT INTO exoapp.passive_mode VALUES (?,?,?);";
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setString(2, date.toString());
            stmt.setInt(3, score);
            stmt.executeUpdate();
        } else if (prevScore < score){
            sql = "UPDATE exoapp.passive_mode SET score=? WHERE user_id=? and date=?;";
            Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, score);
            stmt.setLong(2, userId);
            stmt.setString(3, date.toString());
            stmt.executeUpdate();
        }
    }
}
