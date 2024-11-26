package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.exception.EmployeeNotFoundException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeInMemoryRepository;
import com.oocl.springbootemployee.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
@Service
public class EmployeeService {
    private final EmployeeInMemoryRepository employeeInMemoryRepository;

    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeInMemoryRepository employeeInMemoryRepository, EmployeeRepository employeeRepository) {
        this.employeeInMemoryRepository = employeeInMemoryRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAll(Gender gender) {
        return employeeRepository.findByGender(gender);
    }

    public List<Employee> findAll(Integer page, Integer pageSize) {
        return employeeInMemoryRepository.findAllByPage(page, pageSize);
    }

    public Employee findById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("No such employee"));
    }

    public Employee create(Employee employee) {
        if(employee.getAge() < 18 || employee.getAge() > 65) {
            throw new EmployeeAgeNotValidException();
        }
        if(employee.getAge() >= 30 && employee.getSalary() < 20000.0) {
            throw new EmployeeAgeSalaryNotMatchedException();
        }

        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) {
        Employee employeeExisted = employeeRepository.findById(employee.getId()).orElseThrow(() -> new EmployeeNotFoundException("No such employee"));
        if(Boolean.FALSE.equals(employeeExisted.getActive())) {
            throw new EmployeeInactiveException();
        }

        return employeeRepository.save(employee);
    }

    public void delete(Integer employeeId) {
        employeeInMemoryRepository.deleteById(employeeId);
    }
}
