package org.shlyapa.service;

import org.shlyapa.model.Employee;
import org.shlyapa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Слой бизнес-логики: поиск с фильтрами, сохранение, удаление.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /** Все сотрудники. */
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    /**
     * Поиск сотрудников по части ФИО и признаку активности.
     *
     * @param name   строка поиска (может быть пустой — тогда без поиска по ФИО)
     * @param active "true" / "false" / "all" (или пусто — тогда без фильтра)
     */
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

    /** Возвращает сотрудника по id или выбрасывает исключение, если не найден. */
    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник с id=" + id + " не найден"));
    }

    /** Проверяет, занят ли такой email. */
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    /**
     * Проверяет, занят ли email другим сотрудником
     * (используется при редактировании, чтобы не ругаться на собственный email).
     *
     * @param email   проверяемый email
     * @param currentId id редактируемого сотрудника (может быть null для нового)
     */
    public boolean emailExistsForAnotherEmployee(String email, Long currentId) {
        Employee found = employeeRepository.findByEmailIgnoreCase(email);
        if (found == null) {
            return false;
        }
        return currentId == null || !found.getId().equals(currentId);
    }

    /** Сохраняет сотрудника (создание или обновление). */
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    /** Удаляет сотрудника по id. */
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    /** "true"/"false" -> Boolean, остальное -> null (фильтр не применяется). */
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
