package com.exoapp.database;


import com.exoapp.models.Role;
import com.exoapp.models.User;
import com.exoapp.models.dto.UserInformationDTO;

import java.sql.*;
import java.time.LocalDate;

public class UserDBOp {

    public boolean isUsernameTaken(String username) throws Exception {
        String sql = "SELECT COUNT(*) FROM exoapp.users WHERE username = ?";

        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        else
            throw new Exception("Username is already taken");
    }

    /**
     * Inserts a new user into the database and returns the auto-generated ID.
     * @throws SQLException if insertion fails (e.g., duplicate username).
     */
    public int insertUser(User user) throws Exception {
        if (isUsernameTaken(user.getUsername())) {
            throw new SQLException("Username already exists: " + user.getUsername());
        }

        String sql = "INSERT INTO exoapp.users(username, password, role, pass_change, last_pass_change) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toString());
            stmt.setBoolean(4, false);
            stmt.setDate(5, null);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }
        }
    }

    /**
     * Deletes the user with the id parameter
     * @throws SQLException if insertion fails (e.g., duplicate username).
     */

    public void deleteUser(Long id) throws SQLException {
        String sql = "DELETE FROM exoapp.users WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }


    /**
     * Finds a user by username and returns an instance of the User class
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM exoapp.users WHERE username=?;";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

                int role = Integer.parseInt(rs.getString("role"));
                Role user_role = Role.fromCode(role);
                user.setRole(user_role);

                user.setPasswordChanged(rs.getBoolean("pass_change"));

                Date last_pass_change = rs.getDate("last_pass_change");
                if(last_pass_change != null)
                    user.setLastPasswordChange(rs.getDate("last_pass_change").toLocalDate());
                else
                    user.setLastPasswordChange(null);

                System.out.println("User found: " + user.getUsername());
                System.out.println("Password found: " + user.getPassword());
                return user;
            } else {
                throw new SQLException("User not found: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying database: " + e.getMessage());
        }
        return null;
    }

    /**
     * Returns the id of the user with the username parameter
     * @throws SQLException if the user is not found.
     */
    public Long findIdByUsername(String username) throws SQLException {
        String sql = "SELECT id FROM exoapp.users WHERE username=?;";
        Long id = null;
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            id = rs.getLong(1);
        }
        else {
            throw new SQLException("User not found: " + username);
        }
        return id;
    }

    /**
     * Changes the password of the user with the id parameter
     * @param id
     * @param newPassword
     * @throws SQLException if the user does not exist in the database
     */
    public void changePassword(Long id, String newPassword) throws SQLException {
        String sql = "UPDATE exoapp.users SET password = ?, pass_change=?, last_pass_change=? WHERE id = ?;";
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setBoolean(2, true);
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setLong(4, id);

            stmt.executeUpdate();
    }

    /**
     * Retrieves the information of the user with the userId parameter as a UserinformationDTO object
     * @param userId
     * @return UserinformationDTO object
     * @throws SQLException if the user does not exist in the database
     */
    public UserInformationDTO getUserInformation(Long userId) throws SQLException {
        if (userId == null)
            return null;
        String sql = "SELECT * FROM exoapp.user_information WHERE user_id=?;";
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, String.valueOf(userId));
        ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserInformationDTO userInformationDTO = new UserInformationDTO();
                userInformationDTO.setId(userId);
                userInformationDTO.setName(rs.getString("full_name"));
                userInformationDTO.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                userInformationDTO.setStrokeType(rs.getString("stroke_type"));
                userInformationDTO.setStrokeSubtype(rs.getString("stroke_subtype"));
                userInformationDTO.setStrokeDate(rs.getDate("stroke_date").toLocalDate());
                userInformationDTO.setRehabStartDate(rs.getDate("rehab_start_date").toLocalDate());
                return userInformationDTO;
            }
            else {
                return null;
            }
    }


    public void insertUserInformation(UserInformationDTO dto, String username) throws SQLException {
        Long id = findIdByUsername(username);
        String sql = "INSERT INTO exoapp.user_information VALUES (?, ?, ?, ?, ?, ?, ?);";
        Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, String.valueOf(id));
        stmt.setString(2, dto.getName());
        stmt.setString(3, dto.getDateOfBirth().toString());
        stmt.setString(4, dto.getStrokeType());
       stmt.setString(5, dto.getStrokeSubtype());
       stmt.setString(6, dto.getStrokeDate().toString());
        stmt.setString(7, dto.getRehabStartDate().toString());
        stmt.executeUpdate();
    }

    public void editUserInformation(UserInformationDTO dto) {

        String sql = "UPDATE exoapp.user_information SET full_name = ?, date_of_birth = ?,  stroke_type = ?, stroke_subtype = ?, stroke_date = ?, rehab_start_date = ? WHERE user_id = ? ";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dto.getName());
            stmt.setString(2, dto.getDateOfBirth().toString());
            stmt.setString(3, dto.getStrokeType());
            stmt.setString(4, dto.getStrokeSubtype());
            stmt.setString(5, dto.getStrokeDate().toString());
            stmt.setString(6, dto.getRehabStartDate().toString());
            stmt.setString(7, String.valueOf(dto.getId()));
            stmt.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating database: " + e.getMessage());
        }

    }

    public void insertSupervisorUser(String username, Long supervisorId) throws SQLException {
        Long userId = findIdByUsername(username);

        String sql = "INSERT INTO exoapp.supervisor_user VALUES (?, ?);";

       Connection conn = DatabaseManager.getConnection();
       PreparedStatement stmt = conn.prepareStatement(sql);
       stmt.setString(1, String.valueOf(userId));
       stmt.setString(2, String.valueOf(supervisorId));
       stmt.executeUpdate();
    }


}
