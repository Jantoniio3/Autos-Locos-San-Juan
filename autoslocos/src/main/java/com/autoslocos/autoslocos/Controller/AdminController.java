package com.autoslocos.autoslocos.Controller;


import com.autoslocos.autoslocos.Entity.Vehicle;
import com.autoslocos.autoslocos.Repository.VehicleRepository;
import com.autoslocos.autoslocos.Service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class AdminController {
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    public AdminController(VehicleRepository vehicleRepository, VehicleService vehicleService) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleService =vehicleService;
    }


    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request) {
        // Verificar autenticación (si el usuario está logueado)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");

        // Pasar `admin=true` si está autenticado (todos los usuarios registrados son admins)
        model.addAttribute("admin", isAuthenticated);


        // Lógica original para cargar los vehículos
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Map<String, Object>> vehicleData = new ArrayList<>();

        for (Vehicle v : vehicles) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", v.getId());
            data.put("name", v.getName());
            data.put("driver", v.getDriver());
            data.put("coDriver", v.getCoDriver());
            data.put("passenger1", v.getPassenger1());
            data.put("passenger2", v.getPassenger2());
            data.put("contactNumber", v.getContactNumber());

            if (v.getPhoto() != null && v.getPhoto().length > 0) {
                String base64 = Base64.getEncoder().encodeToString(v.getPhoto());
                String mimeType = v.getPhotoType() != null ? v.getPhotoType() : "image/jpeg";
                data.put("photo", "data:" + mimeType + ";base64," + base64);
            }

            vehicleData.add(data);
        }

        model.addAttribute("vehicles", vehicleData);

        return "admin";
    }
    @PostMapping("/deletevehicle")
    @ResponseBody
    public ResponseEntity<String> deleteVehicle(@RequestParam Long id) {
        try {
            vehicleService.deleteVehicleById(id);
            return ResponseEntity.ok("Vehículo eliminado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar el vehículo");
        }
    }
}
