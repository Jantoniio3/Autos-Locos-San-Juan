package com.autoslocos.autoslocos.Controller;


import com.autoslocos.autoslocos.Entity.Vehicle;
import com.autoslocos.autoslocos.Entity.Sponsor;
import com.autoslocos.autoslocos.Repository.VehicleRepository;
import com.autoslocos.autoslocos.Service.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;
    private final SponsorService sponsorService;

    public AdminController(VehicleRepository vehicleRepository, VehicleService vehicleService, SponsorService sponsorService) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
        this.sponsorService = sponsorService;
    }


    @GetMapping("/admin")
    public String admin(Model model, HttpServletRequest request) {
        // Verificar autenticación (si el usuario está logueado)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);
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
    public String deleteVehicle(@RequestParam Long id) {
        try {
            vehicleService.deleteVehicleById(id);
            return "redirect:/admin";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
    @PostMapping("/addsponsors")
    public String addSponsor(
            @RequestParam("name") String name,
            @RequestPart("logo") MultipartFile logoFile) {
        try {
            byte[] logoBytes = logoFile.getBytes();
            String logoType = logoFile.getContentType();
            Sponsor sponsor = new Sponsor(name, logoBytes, logoType);
            sponsorService.saveSponsor(sponsor);
        } catch (Exception e) {
            return "redirect:/admin?error=true";
        }
        return "redirect:/admin";
    }
}
