package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.CompanyNotFoundException;
import com.oocl.springbootemployee.model.Company;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.repository.CompanyInMemoryRepository;
import com.oocl.springbootemployee.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyInMemoryRepository companyInMemoryRepository;

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyInMemoryRepository companyInMemoryRepository, CompanyRepository companyRepository) {
        this.companyInMemoryRepository = companyInMemoryRepository;
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll(){
        return companyRepository.findAll();
    }

    public Page<Company> findAll(int pageIndex, int pageSize) {
        PageRequest page = PageRequest.of(pageIndex, pageSize);
        Page<Company> companies = companyRepository.findAll(page);
        return new PageImpl<>(companies.getContent(), page, companies.getTotalElements());
    }

    public Company findById(Integer id) {
        return companyInMemoryRepository.findById(id);
    }


    public List<Employee> getEmployeesByCompanyId(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("No such company"));
        return company.getEmployees();
    }

    public Company create(Company company) {
        return companyInMemoryRepository.addCompany(company);
    }

    public Company update(Integer id, Company company) {
        final var companyNeedToUpdate = companyInMemoryRepository
                .findById(id);

        var nameToUpdate = company.getName() == null ? companyNeedToUpdate.getName() : company.getName();
        var employeesToUpdate = company.getEmployees() == null ? companyNeedToUpdate.getEmployees() : company.getEmployees();

        final var companyToUpdate = new Company(id,nameToUpdate,employeesToUpdate);
        return companyInMemoryRepository.updateCompany(id, companyToUpdate);
    }
}
