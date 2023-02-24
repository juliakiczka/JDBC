package org.example.service;

import org.example.dao.DepartmentDao;
import org.example.dao.WorkerDao;

import java.sql.SQLException;
import java.util.Optional;

//purpose of this class is to return worker information including department_name
public class WorkerFullInfoService {
    //    has-a type of composition
    private final WorkerDao workerDao;
    private final DepartmentDao departmentDao;

    public WorkerFullInfoService(WorkerDao workerDao, DepartmentDao departmentDao) {
        this.workerDao = workerDao;
        this.departmentDao = departmentDao;
    }

    public Optional<WorkerWithDepartment> presentFullWorkerDataById(int id) throws SQLException {
        return workerDao.getById(id)
                .map(worker -> {
                    try {
                        return new WorkerWithDepartment(
                                worker.getWorkerId(),
                                worker.getFirstName(),
                                worker.getLastName(),
                                worker.getHireDate(),
                                departmentDao.getById(worker.getDepartmentId())
                                        .map(department -> department.getDepartmentName())
                                        .orElse("Unknown Department")
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
