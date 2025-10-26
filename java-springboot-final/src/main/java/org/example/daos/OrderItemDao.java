package org.example.daos;

import org.example.models.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderItemDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // GET all order items
    public List<OrderItem> getAll() {
        String sql = "SELECT * FROM order_items ORDER BY id;";
        return jdbcTemplate.query(sql, this::mapToOrderItem);
    }

    // GET by ID (null-safe)
    public OrderItem getById(int id) {
        String sql = "SELECT * FROM order_items WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapToOrderItem, id);
        } catch (Exception e) {
            return null;
        }
    }

    // CREATE new order item
    public OrderItem create(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?);";
        int rows = jdbcTemplate.update(sql, item.getOrderId(), item.getProductId(), item.getQuantity());
        return rows > 0 ? getLatest() : null;
    }

    // UPDATE order item (null if not found)
    public OrderItem update(OrderItem item) {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?;";
        int rows = jdbcTemplate.update(sql, item.getOrderId(), item.getProductId(), item.getQuantity(), item.getId());
        return rows > 0 ? getById(item.getId()) : null;
    }

    // DELETE order item
    public int delete(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    // GET most recently inserted item
    private OrderItem getLatest() {
        String sql = "SELECT * FROM order_items ORDER BY id DESC LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapToOrderItem);
        } catch (Exception e) {
            return null;
        }
    }

    // Map SQL result to OrderItem object
    private OrderItem mapToOrderItem(ResultSet rs, int rowNum) throws SQLException {
        return new OrderItem(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity")
        );
    }
}
