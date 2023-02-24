package org.example.dao;

import org.example.dto.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
DAO - Data Access Object class provides operations allowing to Save, Get, Update, Remove (CRUD methods: create, read, update, delete)
implements Repository - Interface that defines common methods
DAO objects operate on DTO (Data Transport Object) objects that are java representation of the DB table
 */

public class DepartmentDao implements Repository<Department> {

    //    Connection object is keeping reference to open DB connection
    //    is used to create Statement(Query or Update) objects
    private final Connection connection;

    public DepartmentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Department save(Department departmentDto) throws SQLException {
//      SQL query with two parameters in form of '?'
//      parameters are used to customize query based on provided
        String createQuery = "INSERT INTO DEPARTMENT (department_id, department_name) VALUES(?,?)";
//      we are always using PreparedStatement that allows to replace '?' with values
        PreparedStatement statement = connection.prepareStatement(createQuery);
//      based on parameterIndex (Index of '?' starting from 1) replace parameter with value
        statement.setInt(1, departmentDto.getDepartmentId());
        statement.setString(2, departmentDto.getDepartmentName());
//      after this operations query will look like this:
//      "INSERT INTO DEPARTMENT (department_id, department_name) VALUES(departmentDto.getDepartmentId(),departmentDto.getDepartmentName())"
//      executeUpdate will return int describing if there were any rows affected by Update operation
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Unable to save Department " + departmentDto.getDepartmentName());
        }
//      this is just to satisfy API, normally we should probably fetch data from DB to be sure if it was saved
        return departmentDto;
    }

    @Override
    public List<Department> getAll() throws SQLException {
        String getAllDepartmentsQuery = "SELECT * FROM department";

        Statement getAllDepartmentsStatement = connection.createStatement();

        ResultSet resultSet = getAllDepartmentsStatement.executeQuery(getAllDepartmentsQuery);

        List<Department> departments = new ArrayList<>();
        while (resultSet.next()) {
            Department department = new Department(
                    resultSet.getInt("department_id"),
                    resultSet.getString("department_name")
            );
            departments.add(department);
        }
        return departments;
    }

    @Override
    public Optional<Department> getById(int id) throws SQLException {
//      SQL query with one parameter '?'
        String getByIdQuery = "SELECT * FROM department where department_id=?";
        PreparedStatement statement = connection.prepareStatement(getByIdQuery);
//      setting query parameter to value id passed to method as an argument
        statement.setInt(1, id);
//      executeQuery() will return ResultSet that represents the rows from DB (DataBase) matching the query from line 69
        ResultSet resultSet = statement.executeQuery();
//      resultSet.next() will point to first row! not zero
        if (resultSet.next()) {
//            based on resultSet (represents values fetched from DB) we will create Department objects (DTO)
            Department department = new Department(
//                    from resultSet we can get DB row values by DB columnIndex or by column name
                    resultSet.getInt(1),
                    resultSet.getString(2)
            );
//            wrapping in optional to indicate that value may be missing
//            null is not a good way to represent lack of value because it just means that object was not initiated
            return Optional.of(department);
        } else {
//     if we are not able to find any item we will return Optional.empty
            return Optional.empty();
        }
    }

    @Override
    public Department update(Department department) throws SQLException {
        String updateQuery = "UPDATE department SET department_name = ? where department_id = ?";
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        // parameterIndex to numer znaku zapytania
        statement.setString(1, department.getDepartmentName());
        statement.setInt(2, department.getDepartmentId());
        int rowsAffected = statement.executeUpdate();
        if (rowsAffected == 0) {
            System.out.println("Department not updated");
        }
        return department;
    }

    //test this!
    @Override
    public boolean removeById(int id) throws SQLException {
        String removeByIdQuery = "DELETE FROM department WHERE department_id=?";
        PreparedStatement statement = connection.prepareStatement(removeByIdQuery);
        statement.setInt(1, id);
        int rowsAffected = statement.executeUpdate();
        if (rowsAffected == 0) {
            System.out.println("System was unable to delete an entry department at id: " + id);
            return false;
        } else {
            System.out.format("The entry of department_id %d has been deleted", id);
            return true;
        }
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}