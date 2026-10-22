package org.shlyapa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Перенаправление с главной страницы на список сотрудников,
 * чтобы приложение открывалось сразу по адресу http://localhost:8080/.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/employees";
    }
}
