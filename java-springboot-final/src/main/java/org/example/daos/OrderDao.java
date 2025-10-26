package org.example.daos;

import org.example.models.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // GET all orders
    public List<Order> getAll() {
        String sql = "SELECT * FROM orders ORDER BY id;";
        return jdbcTemplate.query(sql, this::mapToOrder);
    }

    // GET by ID (null-safe)
    public Order getById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapToOrder, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // CREATE new order
    public Order create(Order order) {
        String sql = "INSERT INTO orders (username) VALUES (?);";
        int rows = jdbcTemplate.update(sql, order.getUsername());
        return rows > 0 ? getLatest() : null;
    }

    // UPDATE order (null if not found)
    public Order update(Order order) {
        String sql = "UPDATE orders SET username = ? WHERE id = ?;";
        int rows = jdbcTemplate.update(sql, order.getUsername(), order.getId());
        return rows > 0 ? getById(order.getId()) : null;
    }

    // DELETE order
    public int delete(int id) {
        String sql = "DELETE FROM orders WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    // GET most recently inserted order
    private Order getLatest() {
        String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapToOrder);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Map SQL result to Order object
    private Order mapToOrder(ResultSet rs, int rowNum) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getString("username")
        );
    }
}
