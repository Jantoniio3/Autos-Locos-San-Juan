package com.autoslocos.autoslocos.Controller;

import com.autoslocos.autoslocos.Entity.Vehicle;
import com.autoslocos.autoslocos.Service.VehicleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MustacheController {

    private final VehicleService vehicleService;

    public MustacheController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

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

    @GetMapping("/inscriptions")
    public String inscriptions(Model model) {
        return "inscriptions";
    }
    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin";
    }

    @PostMapping("/addvehicle")
    public String addVehicle(
            @RequestParam String name,
            @RequestParam String driver,
            @RequestParam String contactNumber,
            @RequestParam(required = false) String coDriver,
            @RequestParam(required = false) String passenger1,
            @RequestParam(required = false) String passenger2,
            @RequestParam MultipartFile photo,
            RedirectAttributes redirectAttributes) {

        try {
            Vehicle vehicle = vehicleService.addVehicleWithPassengers(
                    name, driver, contactNumber, 
                    coDriver, passenger1, passenger2, 
                    photo);
            
            redirectAttributes.addFlashAttribute("success", "¡Vehículo registrado exitosamente!");
            return "redirect:/";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar el vehículo: " + e.getMessage());
            return "redirect:/inscriptions";
        }
    }
}