package org.shlyapa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Модель данных «Сотрудник» — соответствует таблице EMPLOYEE в БД.
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "ФИО не может быть пустым")
    @Size(min = 3, max = 100, message = "ФИО должно содержать от 3 до 100 символов")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email имеет неверный формат (пример: ivan@mail.ru)")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Телефон не может быть пустым")
    @Pattern(regexp = "^\\+7\\d{10}$|^8\\d{10}$",
            message = "Телефон должен быть в формате +7XXXXXXXXXX или 8XXXXXXXXXX")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "Должность не может быть пустой")
    @Size(max = 60, message = "Должность не должна превышать 60 символов")
    @Column(name = "position", nullable = false, length = 60)
    private String position;

    @NotNull(message = "Зарплата не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = false, message = "Зарплата должна быть больше 0")
    @Column(name = "salary", nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @NotNull(message = "Признак активности обязателен")
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Employee() {
    }

    public Employee(String fullName, String email, String phone, String position,
                    BigDecimal salary, Boolean active) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee employee)) {
            return false;
        }
        return id != null && id.equals(employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", fullName='" + fullName + "', email='" + email + "'}";
    }
}
