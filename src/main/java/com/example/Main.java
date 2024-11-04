package com.example;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.example.models.Employee;
import com.example.postgresql.DatabaseManager;

public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/db";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";
    private static final String[] LAST_NAMES = { "Fateev", "Fedorov", "Fedosov", "Fedotov", "Feldman", "Filippov",
            "Firsov", "Fokina", "Fomin", "Frolov" };

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Не указаны параметры.");
            return;
        }

        String mode = args[0];

        try {
            DatabaseManager dbManager = new DatabaseManager(DB_URL, DB_USER, DB_PASSWORD);

            switch (mode) {
                case "1":
                    dbManager.createEmployeeTable();
                    System.out.println("Таблица сотрудников создана.");
                    break;

                case "2":
                    String fullName = args[1];
                    LocalDate birthDate = LocalDate.parse(args[2]);
                    String gender = args[3];

                    Employee employee = new Employee(fullName, birthDate, gender);
                    dbManager.insertEmployee(employee);
                    System.out.println("Запись добавлена: " + employee);
                    break;

                case "3":
                    List<Employee> employees = dbManager.getAllEmployeesSorted();
                    employees.forEach(System.out::println);
                    break;

                case "4":
                    List<Employee> generateEmployees = generateEmployees(1000000);
                    for (Employee generateEmployee : generateEmployees) {
                        dbManager.insertEmployee(generateEmployee);
                    }
                    System.out.println("Сгенерировано 1000000 сотрудников.");
                    break;

                case "5":
                    long startTime = System.currentTimeMillis();
                    List<Employee> filteredEmployees = dbManager.getFilteredEmployees("Male", "F");
                    long endTime = System.currentTimeMillis();
                    filteredEmployees.forEach(System.out::println);
                    System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
                    break;

                default:
                    System.out.println("Неизвестный режим.");
                    break;
            }

            dbManager.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static List<Employee> generateEmployees(int count) {
        List<Employee> employees = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2015, 12, 31).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            Employee employee = new Employee(lastName, randomDate, random.nextBoolean() ? "Male" : "Female");
            employees.add(employee);
        }
        return employees;
    }
}