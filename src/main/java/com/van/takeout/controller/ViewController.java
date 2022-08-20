package com.van.takeout.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping
    public String toIndex() {
        return "redirect:/backend/index.html";
    }
}
