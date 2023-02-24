package org.example;

import org.example.dao.DepartmentDao;
import org.example.dao.WorkerDao;
import org.example.dto.Department;
import org.example.dto.Worker;
import org.example.service.WorkerFullInfoService;
import org.example.service.WorkerWithDepartment;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
public class Main {
    public static void main(String[] args) {
        WorkerWithDepartment workerWithDepartment = new WorkerWithDepartment(
                4,"Elon","Musk",LocalDate.parse("2022-01-01"),"Twitter");
        System.out.println(workerWithDepartment);
        System.out.println(workerWithDepartment.departmentName());
        workerWithDepartment.hello();
//        how to modify Record
        WorkerWithDepartment modified = new WorkerWithDepartment(
                workerWithDepartment.workerId(),
                workerWithDepartment.firstName(),
                workerWithDepartment.lastName(),
                workerWithDepartment.hireDate(),
                "Tesla"

        );
        System.out.println(modified);
//        Garbage Collector
        LocalDate nowPlus5days = LocalDate.now().plusDays(5);

        //Creating jdbc connection to MySql db, providing user and password.
        //Connection and DriverManager are part of java.sql package
        //In order to make it work we need to add mysql-connector-java dependency in the pom file
        System.out.println("Hello world!");
        System.out.println("-----||-----");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/jdbc", "root", "Julia160703!"
        )) {
//          drop and reinitialize tables
            DataInitializer dataInitializer = new DataInitializer(connection);
            dataInitializer.initData();
//          creating DAO object for CRUD operations on department table
            DepartmentDao departmentDao = new DepartmentDao(connection);
            departmentDao.save(new Department(1, "Ministry of Magic"));

            Department ministryOfMysteries = new Department(2, "Ministry of Mysteries");
            departmentDao.save(ministryOfMysteries);

            System.out.println(departmentDao.getAll());

            Optional<Department> optionalDepartment = departmentDao.getById(5);

            System.out.println(optionalDepartment);

            ministryOfMysteries.setDepartmentName("Ministry of Something else");
            departmentDao.update(ministryOfMysteries);

            System.out.println( departmentDao.getById(ministryOfMysteries.getDepartmentId()));

            WorkerDao workerDao = new WorkerDao(connection);
            Worker worker = new Worker(1,"Petter","Gibbons", LocalDate.of(2022,1,1),1);
            workerDao.save(worker);
            System.out.println(workerDao.getById(1));

            WorkerFullInfoService workerFullInfoService = new WorkerFullInfoService(workerDao,departmentDao);
            System.out.println(workerFullInfoService.presentFullWorkerDataById(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}