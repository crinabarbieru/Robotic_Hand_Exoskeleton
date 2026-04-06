package com.exoapp.database;

import com.exoapp.models.dto.ProgressTrackerDTO;
import com.exoapp.models.dto.SupervisorStatsDTO;
import com.exoapp.models.dto.UserInformationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupervisorDBOp {

    public List<UserInformationDTO> getMyPatients(Long supervisorId) throws SQLException {
        List<UserInformationDTO> myPatients = new ArrayList<>();
        String sql = "select ui.user_id, full_name, date_of_birth, stroke_type, stroke_subtype, stroke_date, rehab_start_date from exoapp.user_information ui join exoapp.supervisor_user su on ui.user_id=su.user_id where su.supervisor_id= ?;";

        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, supervisorId.toString());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Long userId = rs.getLong("user_id");
            String fullName = rs.getString("full_name");
            LocalDate dateOfBirth = LocalDate.parse(rs.getString("date_of_birth"));
            String strokeType = rs.getString("stroke_type");
            String strokeSubtype = rs.getString("stroke_subtype");
            LocalDate strokeDate = LocalDate.parse(rs.getString("stroke_date"));
            LocalDate rehabStartDate = LocalDate.parse(rs.getString("rehab_start_date"));
            UserInformationDTO myPatient = new UserInformationDTO(userId, fullName, dateOfBirth, strokeType, strokeSubtype, strokeDate, rehabStartDate);
            myPatients.add(myPatient);
        }
        return myPatients;
    }

    public List<ProgressTrackerDTO> getPatientsProgress(Long supervisorId) throws SQLException {
        List<ProgressTrackerDTO> patientsProgress = new ArrayList<>();
        String sql_info = "SELECT ui.user_id, full_name, stroke_date, rehab_start_date FROM exoapp.user_information ui JOIN exoapp.supervisor_user su on ui.user_id=su.user_id WHERE su.supervisor_id= ?;";
        String sql_sessions = "SELECT * FROM exoapp.exercise_sessions WHERE user_id = ?";
        String sql_passive = "SELECT * FROM exoapp.passive_mode WHERE user_id = ?";

        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt_info = conn.prepareStatement(sql_info);
        stmt_info.setString(1, supervisorId.toString());
        ResultSet rs_info = stmt_info.executeQuery();
        while (rs_info.next()) {
            ProgressTrackerDTO progressTrackerDTO = new ProgressTrackerDTO();
            Long userId = rs_info.getLong("user_id");
            progressTrackerDTO.setId(userId);
            String fullName = rs_info.getString("full_name");
            progressTrackerDTO.setName(fullName);
            LocalDate strokeDate = rs_info.getDate("stroke_date").toLocalDate();
            progressTrackerDTO.setStrokeDate(strokeDate);
            LocalDate rehabstartDate = rs_info.getDate("rehab_start_date").toLocalDate();
            progressTrackerDTO.setRehabStartDate(rehabstartDate);

            Map<LocalDate, Float> sessions = new HashMap<>();
            PreparedStatement stmt_sessions = conn.prepareStatement(sql_sessions);
            stmt_sessions.setLong(1, userId);
            ResultSet rs_sessions = stmt_sessions.executeQuery();
            while (rs_sessions.next()) {
                LocalDate sessionDate = LocalDate.parse(rs_sessions.getString("date"));
                Float sessionDuration = rs_sessions.getFloat("duration");
                sessions.put(sessionDate, sessionDuration);
            }
            progressTrackerDTO.setSessions(sessions);

            Map<LocalDate, Integer> passiveSessions = new HashMap<>();
            PreparedStatement stmt_passive = conn.prepareStatement(sql_passive);
            stmt_passive.setLong(1, userId);
            ResultSet rs_passive = stmt_passive.executeQuery();
            while (rs_passive.next()) {
                LocalDate passiveDate = LocalDate.parse(rs_passive.getString("date"));
                Integer score = rs_passive.getInt("score");
                passiveSessions.put(passiveDate, score);
            }
            progressTrackerDTO.setPassiveScores(passiveSessions);
            patientsProgress.add(progressTrackerDTO);
        }
        return  patientsProgress;
    }

    public SupervisorStatsDTO getSupervisorStats(Long supervisorId) throws SQLException {
        SupervisorStatsDTO supervisorStats = new SupervisorStatsDTO();
        int patientsCount = getPatientsCount(supervisorId);
        supervisorStats.setTotalPatients(patientsCount);
        int sessionsThisWeek = getSessionsThisWeek(supervisorId);
        supervisorStats.setSessionsThisWeek(sessionsThisWeek);
        float totalMinutes = getMinutesThisWeek(supervisorId);
        supervisorStats.setTotalMinutes(totalMinutes);
        float avgSessionDuration = totalMinutes / sessionsThisWeek;
        supervisorStats.setAvgSessionDuration(avgSessionDuration);
        Map<String, Integer> ageDistribution = getAgeDistribution(supervisorId);
        supervisorStats.setAgeDistribution(ageDistribution);
        return supervisorStats;
    }

    public int getPatientsCount(Long supervisorId) throws SQLException {
        String sql = "select count(*) from supervisor_user where supervisor_id= ?";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supervisorId.toString());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new SQLException("Could not fetch number of patients");
        }
    }
    public int getSessionsThisWeek(Long supervisorId) throws SQLException {
        String sql = "select count(*) from exoapp.exercise_sessions es join exoapp.supervisor_user su on es.user_id=su.user_id where supervisor_id= ? and yearweek(date, 1) = yearweek(curdate(), 1);";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supervisorId.toString());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new SQLException("Could not fetch sessions.");
        }
    }
    public float getMinutesThisWeek(Long supervisorId) throws SQLException {
        String sql = "select sum(duration) from exoapp.exercise_sessions es join exoapp.supervisor_user su on es.user_id=su.user_id where supervisor_id= ? and yearweek(date, 1) = yearweek(curdate(), 1);";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, supervisorId.toString());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getFloat(1);
        } catch (SQLException e) {
            throw new SQLException("Could not fetch minutes.");
        }
    }
    public Map<String, Integer> getAgeDistribution(Long supervisorId) throws SQLException {
        Map<String, Integer> ageDistribution = new HashMap<>();
        ageDistribution.put("18-30", 0);
        ageDistribution.put("31-45", 0);
        ageDistribution.put("46-60", 0);
        ageDistribution.put("60+", 0);
        String sql = "SELECT CASE WHEN TIMESTAMPDIFF(YEAR, ui.date_of_birth, CURDATE()) BETWEEN 18 AND 30 THEN '18-30' WHEN TIMESTAMPDIFF(YEAR, ui.date_of_birth, CURDATE()) BETWEEN 31 AND 45 THEN '31-45' WHEN TIMESTAMPDIFF(YEAR, ui.date_of_birth, CURDATE()) BETWEEN 46 AND 60 THEN '46-60' ELSE '60+' END as ageGroup, COUNT(*) as count FROM user_information ui join supervisor_user su on ui.user_id=su.user_id where supervisor_id = ? GROUP BY ageGroup";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supervisorId.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ageGroup = rs.getString("ageGroup");
                int count = rs.getInt("count");
                ageDistribution.put(ageGroup, count);
            }
            return ageDistribution;
        }catch(SQLException e) {
            throw new SQLException("Could not fetch age distribution.");
        }
    }
}
