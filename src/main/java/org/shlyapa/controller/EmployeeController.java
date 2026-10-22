package org.shlyapa.controller;

import jakarta.validation.Valid;
import org.shlyapa.model.Employee;
import org.shlyapa.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "active", required = false) String active,
                       Model model) {
        List<Employee> employees = employeeService.search(name, active);
        model.addAttribute("employees", employees);
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("active", active == null ? "all" : active);
        model.addAttribute("count", employees.size());
        return "employees-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }
        model.addAttribute("editing", false);
        return "employee-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("employee") @Valid Employee employee,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,
                      Model model) {
        if (employee.getEmail() != null
                && employeeService.emailExists(employee.getEmail().trim())) {
            bindingResult.rejectValue("email", "error.employee",
                    "Сотрудник с таким email уже существует");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("editing", false);
            return "employee-form";
        }
        if (employee.getActive() == null) {
            employee.setActive(true);
        }
        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("message",
                "Сотрудник «" + employee.getFullName() + "» успешно добавлен");
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Employee employee;
        try {
            employee = employeeService.getById(id);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", employee);
        }
        model.addAttribute("editing", true);
        return "employee-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,
                       @ModelAttribute("employee") @Valid Employee employee,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       Model model) {
        Employee existing;
        try {
            existing = employeeService.getById(id);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
        if (employee.getEmail() != null
                && employeeService.emailExistsForAnotherEmployee(employee.getEmail().trim(), id)) {
            bindingResult.rejectValue("email", "error.employee",
                    "Сотрудник с таким email уже существует");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("editing", true);
            return "employee-form";
        }
        existing.setFullName(employee.getFullName());
        existing.setEmail(employee.getEmail());
        existing.setPhone(employee.getPhone());
        existing.setPosition(employee.getPosition());
        existing.setSalary(employee.getSalary());
        existing.setActive(employee.getActive() != null ? employee.getActive() : Boolean.TRUE);
        employeeService.save(existing);
        redirectAttributes.addFlashAttribute("message",
                "Данные сотрудника «" + existing.getFullName() + "» обновлены");
        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.getById(id);
            employeeService.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "Сотрудник «" + employee.getFullName() + "» удалён");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employees";
    }
}
