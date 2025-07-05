package com.autoslocos.autoslocos.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
