package com.example.postgresql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.models.Employee;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public void createEmployeeTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS employees (" + "full_name VARCHAR(255), " + "birth_date DATE, "
                + "gender VARCHAR(10), " + "PRIMARY KEY (full_name, birth_date))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void insertEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (full_name, birth_date, gender) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFullName());
            pstmt.setDate(2, Date.valueOf(employee.getBirthDate()));
            pstmt.setString(3, employee.getGender());
            pstmt.executeUpdate();
        }
    }

    public List<Employee> getAllEmployeesSorted() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY full_name";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String fullName = rs.getString("full_name");
                LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                String gender = rs.getString("gender");
                employees.add(new Employee(fullName, birthDate, gender));
            }
        }
        return employees;
    }

    public List<Employee> getFilteredEmployees(String gender, String lastNamePrefix) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE gender = ? AND full_name LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, gender);
            pstmt.setString(2, lastNamePrefix + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String fullName = rs.getString("full_name");
                    LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                    employees.add(new Employee(fullName, birthDate, gender));
                }
            }
        }
        return employees;
    }

    public void close() throws SQLException {
        if (connection != null)
            connection.close();
    }
}