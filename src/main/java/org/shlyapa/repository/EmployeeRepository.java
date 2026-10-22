package org.shlyapa.repository;

import org.shlyapa.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Слой работы с базой данных.
 * Spring Data JPA сам реализует методы по их именам.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /** Проверка существования по email. */
    boolean existsByEmail(String email);

    /** Поиск по email, игнорируя регистр. */
    Employee findByEmailIgnoreCase(String email);

    /** Поиск по фамилии (части ФИО), регистронезависимый. */
    List<Employee> findByFullNameContainingIgnoreCase(String fullNamePart);

    /** Поиск по признаку активности. */
    List<Employee> findByActive(Boolean active);

    /** Поиск одновременно по фамилии и активности. */
    List<Employee> findByFullNameContainingIgnoreCaseAndActive(String fullNamePart, Boolean active);
}
