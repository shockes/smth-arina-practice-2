package org.shlyapa.config;

import org.shlyapa.model.Employee;
import org.shlyapa.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Заполняет базу тестовыми сотрудниками при старте приложения,
 * чтобы список не был пустым и можно было сразу проверить поиск и фильтры.
 */
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(EmployeeRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.save(new Employee("Иванов Иван Иванович", "ivanov@mail.ru",
                    "+79001234567", "Программист", new BigDecimal("120000.00"), true));
            repository.save(new Employee("Петрова Анна Сергеевна", "petrova@mail.ru",
                    "+79007654321", "Аналитик", new BigDecimal("95000.00"), true));
            repository.save(new Employee("Сидоров Пётр Алексеевич", "sidorov@mail.ru",
                    "89001112233", "Тестировщик", new BigDecimal("80000.00"), false));
            repository.save(new Employee("Кузнецова Мария Олеговна", "kuznetsova@mail.ru",
                    "+79005556677", "Менеджер", new BigDecimal("70000.00"), true));
        };
    }
}
