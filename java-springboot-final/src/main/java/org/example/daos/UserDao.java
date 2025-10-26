package org.example.daos;

import org.example.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // GET all users
    public List<User> getAll() {
        String sql = "SELECT * FROM users ORDER BY username;";
        return jdbcTemplate.query(sql, this::mapToUser);
    }

    // GET user by username (null-safe)
    public User getByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?;";
        List<User> users = jdbcTemplate.query(sql, this::mapToUser, username);
        return users.isEmpty() ? null : users.get(0);
    }

    // CREATE new user
    public User create(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?);";
        int rows = jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
        return rows > 0 ? getByUsername(user.getUsername()) : null;
    }

    // UPDATE user password
    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?;";
        int rows = jdbcTemplate.update(sql, newPassword, username);
        return rows > 0;
    }

    // UPDATE user (currently only updates password)
    public User update(User user) {
        String sql = "UPDATE users SET password = ? WHERE username = ?;";
        int rows = jdbcTemplate.update(sql, user.getPassword(), user.getUsername());
        return rows > 0 ? getByUsername(user.getUsername()) : null;
    }

    // DELETE user
    public int delete(String username) {
        String sql = "DELETE FROM users WHERE username = ?;";
        return jdbcTemplate.update(sql, username);
    }

    // GET roles for a user
    public List<String> getRoles(String username) {
        String sql = "SELECT role FROM roles WHERE username = ?;";
        return jdbcTemplate.queryForList(sql, String.class, username);
    }

    // ADD role to a user
    public List<String> addRole(String username, String role) {
        String sql = "INSERT INTO roles (username, role) VALUES (?, ?);";
        jdbcTemplate.update(sql, username, role);
        return getRoles(username);
    }

    // DELETE role from a user
    public int deleteRole(String username, String role) {
        String sql = "DELETE FROM roles WHERE username = ? AND role = ?;";
        return jdbcTemplate.update(sql, username, role);
    }

    // Map SQL result to User object
    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getString("username"),
                rs.getString("password")
        );
    }
}
