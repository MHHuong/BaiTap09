package vn.host.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/products")
    public String products() { return "products"; }

    @GetMapping("/products-by-category")
    public String productsByCategory() { return "productsByCategory"; }

    @GetMapping("/admin")
    public String admin() { return "admin"; }
}