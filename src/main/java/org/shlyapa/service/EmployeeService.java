package org.shlyapa.service;

import org.shlyapa.model.Employee;
import org.shlyapa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> search(String name, String active) {
        boolean hasName = name != null && !name.isBlank();
        Boolean activeFlag = parseActive(active);

        if (hasName && activeFlag != null) {
            return employeeRepository.findByFullNameContainingIgnoreCaseAndActive(name.trim(), activeFlag);
        }
        if (hasName) {
            return employeeRepository.findByFullNameContainingIgnoreCase(name.trim());
        }
        if (activeFlag != null) {
            return employeeRepository.findByActive(activeFlag);
        }
        return employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник с id=" + id + " не найден"));
    }

    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public boolean emailExistsForAnotherEmployee(String email, Long currentId) {
        Employee found = employeeRepository.findByEmailIgnoreCase(email);
        if (found == null) {
            return false;
        }
        return currentId == null || !found.getId().equals(currentId);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private Boolean parseActive(String active) {
        if (active == null || active.isBlank() || "all".equalsIgnoreCase(active)) {
            return null;
        }
        if ("true".equalsIgnoreCase(active) || "active".equalsIgnoreCase(active)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(active) || "inactive".equalsIgnoreCase(active)) {
            return Boolean.FALSE;
        }
        return null;
    }
}
