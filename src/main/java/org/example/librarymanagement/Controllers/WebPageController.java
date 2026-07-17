package org.example.librarymanagement.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    @GetMapping(value = {"/", "/login", "/user", "/staff", "/admin"})
    public String index() {
        return "index";
    }
}
