package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataInitializer {

    private Connection connection;

    public DataInitializer(Connection connection) {
        this.connection = connection;
    }

    public void initData() throws SQLException {
        //change to prepared statement
        String dropWorkerQuery = "DROP TABLE IF EXISTS worker";
        PreparedStatement dropWorkerStatement = connection.prepareStatement(dropWorkerQuery);
        dropWorkerStatement.execute();

        String dropQuery = "DROP TABLE IF EXISTS department";
        Statement dropTableStatement = connection.createStatement();
        dropTableStatement.execute(dropQuery);

        String createDepartmentQuery = """
                    CREATE TABLE IF NOT EXISTS department (
                        department_id int primary key,
                        department_name varchar(50)
                    );""";
        Statement createTableStatement = connection.createStatement();
        createTableStatement.execute(createDepartmentQuery);



        String createWorkerQuery = """
                CREATE TABLE IF NOT EXISTS worker (
                    worker_id int primary key,
                    first_name varchar(50) NOT NULL,
                    last_name varchar(50) NOT NULL,
                    hire_date date,
                    department_id int,
                    FOREIGN KEY (department_id) REFERENCES department(department_id)
                );
                """;

        PreparedStatement createWorker = connection.prepareStatement(createWorkerQuery);
        createWorker.execute();
    }
}