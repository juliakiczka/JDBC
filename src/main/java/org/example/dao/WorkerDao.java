package org.example.dao;

import org.example.dto.Department;
import org.example.dto.Worker;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkerDao implements Repository<Worker> {

    private final Connection connection;

    public WorkerDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Worker save(Worker worker) throws SQLException {
        String saveQuery = """
                INSERT INTO worker (worker_id, first_name, last_name, hire_date, department_id) 
                VALUES (?,?,?,?,?) 
                """;
        PreparedStatement statement = connection.prepareStatement(saveQuery);
        statement.setInt(1, worker.getWorkerId());
        statement.setString(2, worker.getFirstName());
        statement.setString(3, worker.getLastName());
        statement.setDate(4, Date.valueOf(worker.getHireDate()));
        statement.setInt(5, worker.getDepartmentId());
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Failed to save worker");
        }
        return worker;
    }

    @Override
    public List<Worker> getAll() throws SQLException {
//        String getAllWorkersQuery = "SELECT * FROM worker";
//
//        Statement getAllWorkersStatement = connection.createStatement();
//
//        ResultSet resultSet = getAllWorkersStatement.executeQuery(getAllWorkersQuery);
//
//        List<Worker> workers = new ArrayList<>();
//        while (resultSet.next()) {
//            Worker worker = new Worker(
//                    resultSet.getInt("worker_id"),
//                    resultSet.getString("first_name"),
//                    resultSet.getString("last_name"),
//                    LocalDate.parse(resultSet.getString("hire_date")),
////                    resultSet.getString("department_id")
//            );
//            workers.add(worker);
//        }
        return null;
    }

    @Override
    public Optional<Worker> getById(int id) throws SQLException {
        String getByIdQuery = "SELECT * FROM worker WHERE worker_id = ?";
        PreparedStatement statement = connection.prepareStatement(getByIdQuery);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
//        w ResultSet odwo??ujemy si?? do kolumn
//        w Statement odwo??ujemy si?? do zapyta??
        if (resultSet.next()) {
            Worker worker = new Worker(
                    resultSet.getInt("worker_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    LocalDate.parse(resultSet.getDate("hire_date").toString()),
                    resultSet.getInt("department_id")
            );
            return Optional.of(worker);
        }
        return Optional.empty();
    }

    @Override
    public Worker update(Worker worker) throws SQLException {
        return null;
    }

    @Override
    public boolean removeById(int id) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
