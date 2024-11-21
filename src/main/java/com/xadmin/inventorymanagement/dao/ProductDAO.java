package com.xadmin.inventorymanagement.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.xadmin.inventorymanagement.bean.Product;


public class ProductDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/products?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "password";
    private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

    private static final String INSERT_PRODUCT_SQL = "INSERT INTO product (Name, Price, Quantity) VALUES (?, ?, ?);";
    private static final String SELECT_PRODUCT_BY_ID = "SELECT id, Name, Price, Quantity FROM product WHERE id =?;";
    private static final String SELECT_ALL_PRODUCTS = "SELECT * FROM product;";
    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE id =?;";
    private static final String UPDATE_PRODUCT = "UPDATE product SET Name=?, Price=?, Quantity=? WHERE id=?;";

    public ProductDAO() {}

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Replace with logging
        }
        return connection;
    }

    public void insertProduct(Product product) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.executeUpdate();
        }
    }

    public Product selectProduct(int id) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                int price = rs.getInt("Price");
                int quantity = rs.getInt("Quantity");
                product = new Product(id, name, price, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logging
        }
        return product;
    }

    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("Name");
                int price = rs.getInt("Price");
                int quantity = rs.getInt("Quantity");
                products.add(new Product(id, name, price, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logging
        }
        return products;
    }

    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setInt(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getId());
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
}
