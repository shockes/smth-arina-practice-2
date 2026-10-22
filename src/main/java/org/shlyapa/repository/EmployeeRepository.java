package org.shlyapa.repository;

import org.shlyapa.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Employee findByEmailIgnoreCase(String email);

    List<Employee> findByFullNameContainingIgnoreCase(String fullNamePart);

    List<Employee> findByActive(Boolean active);

    List<Employee> findByFullNameContainingIgnoreCaseAndActive(String fullNamePart, Boolean active);
}
