package org.example.daos;

import org.example.models.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // GET all products
    public List<Product> getAll() {
        String sql = "SELECT * FROM products ORDER BY id;";
        return jdbcTemplate.query(sql, this::mapToProduct);
    }

    // GET product by ID (null-safe)
    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapToProduct, id);
        } catch (EmptyResultDataAccessException e) {
            return null; // ✅ Prevents 500 error if product not found
        }
    }

    // CREATE new product
    public Product create(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?);";
        int rows = jdbcTemplate.update(sql, product.getName(), product.getPrice());
        return rows > 0 ? getLatest() : null;
    }

    // UPDATE product (returns null if not found)
    public Product update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?;";
        int rows = jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getId());
        return rows > 0 ? getById(product.getId()) : null;
    }

    // DELETE product
    public int delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    // GET most recently inserted product (null-safe)
    private Product getLatest() {
        String sql = "SELECT * FROM products ORDER BY id DESC LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapToProduct);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Map SQL result to Product object
    private Product mapToProduct(ResultSet rs, int rowNum) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price")
        );
    }
}
