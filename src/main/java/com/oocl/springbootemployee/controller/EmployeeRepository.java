package com.oocl.springbootemployee.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeRepository() {
        initEmployeeData();
    }

    public List<Employee> getAll() {
        return this.employees;
    }

    private void initEmployeeData() {
        this.employees.add(new Employee(1, "John Smith", 32, Gender.MALE, 5000.0));
        this.employees.add(new Employee(2, "Jane Johnson", 28, Gender.FEMALE, 6000.0));
        this.employees.add(new Employee(3, "David Williams", 35, Gender.MALE, 5500.0));
        this.employees.add(new Employee(4, "Emily Brown", 23, Gender.FEMALE, 4500.0));
        this.employees.add(new Employee(5, "Michael Jones", 40, Gender.MALE, 7000.0));
    }

    public Employee getEmployeeById(Integer id) {
        return employees.stream()
            .filter(employee -> Objects.equals(employee.getId(), id))
            .findFirst()
            .orElse(null);
    }

    public List<Employee> getEmployeesByGender(Gender gender) {
        return employees.stream()
            .filter(employee -> employee.getGender().equals(gender))
            .collect(Collectors.toList());
    }

    public Employee addEmployee(Employee employee) {
        final Employee newEmployee = new Employee(
            this.getAll().size() + 1,
            employee.getName(),
            employee.getAge(),
            employee.getGender(),
            employee.getSalary());

        employees.add(newEmployee);
        return newEmployee;
    }

    public Employee updateEmployee(Integer id, Employee employee) {
        return employees.stream()
            .filter(storedEmployee -> storedEmployee.getId().equals(id))
            .findFirst()
            .map(storedEmployee -> updateEmployeeAttributes(storedEmployee, employee))
            .orElse(null);
    }

    private Employee updateEmployeeAttributes(Employee employeeStored, Employee newEmployee) {
        if (newEmployee.getName() != null) {
            employeeStored.setName(newEmployee.getName());
        }
        if (newEmployee.getAge() != null) {
            employeeStored.setAge(newEmployee.getAge());
        }
        if (newEmployee.getGender() != null) {
            employeeStored.setGender(newEmployee.getGender());
        }
        if (newEmployee.getSalary() != null) {
            employeeStored.setSalary(newEmployee.getSalary());
        }
        return employeeStored;
    }

    public void removeEmployee(Integer id) {
        employees.removeIf(employee -> Objects.equals(employee.getId(), id));
    }

    public List<Employee> getAllByPageSize(Integer pageIndex, Integer pageSize) {
        return employees.stream()
            .skip((long) (pageIndex - 1) * pageSize)
            .limit(pageSize)
            .toList();
    }
}