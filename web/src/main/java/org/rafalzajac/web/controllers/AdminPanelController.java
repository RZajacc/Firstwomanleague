package org.rafalzajac.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminPanelController {

    @GetMapping("/admin")
    public String adminPanel() {
        return "administration/adminPanel";
    }
}
