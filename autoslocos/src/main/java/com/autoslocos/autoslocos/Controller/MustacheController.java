package com.autoslocos.autoslocos.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MustacheController {

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }
    
    @GetMapping("/")
    public String def(Model model){
        return "index";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        return "contact";
    }
    @GetMapping("/newness")
    public String newness(Model model){
        return "newness";
    }
    @GetMapping("/login")
    public String login(Model model){
        return "Login";
    }


}
