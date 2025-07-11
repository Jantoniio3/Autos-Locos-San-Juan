package com.autoslocos.autoslocos.Controller;

import com.autoslocos.autoslocos.Entity.Vehicle;
import com.autoslocos.autoslocos.Service.VehicleService;
import com.autoslocos.autoslocos.Service.SponsorService;
import com.autoslocos.autoslocos.Entity.Sponsor;
import com.autoslocos.autoslocos.Entity.Image;
import com.autoslocos.autoslocos.Repository.ImageRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;


@Controller
public class MustacheController {

    private final VehicleService vehicleService;
    private final SponsorService sponsorService;
    private final ImageRepository imageRepository;

    public MustacheController(VehicleService vehicleService, SponsorService sponsorService, ImageRepository imageRepository) {
        this.vehicleService = vehicleService;
        this.sponsorService = sponsorService;
        this.imageRepository = imageRepository;
    }

    // Elimina el @ModelAttribute de aquí y usa el GlobalControllerAdvice
    
    @GetMapping("/inicio")
    public String index(Model model) {
        return "index";
    }
    
    @GetMapping("/")
    public String def(Model model){
        return "index";
    }

    @GetMapping("/contacto")
    public String contact(Model model){
        return "contact";
    }

    @GetMapping("/novedades")
    public String newness(Model model){
        return "newness";
    }
    @GetMapping("/patrocinadores")
    public String sponsors(Model model, HttpServletRequest request) {
        List<Sponsor> sponsors = sponsorService.getAllSponsors();
        List<Map<String, Object>> sponsorData = new ArrayList<>();
        for (Sponsor s : sponsors) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", s.getId());
            data.put("name", s.getName());
            if (s.getLogo() != null && s.getLogo().length > 0) {
                String base64 = Base64.getEncoder().encodeToString(s.getLogo());
                String mimeType = s.getLogoType() != null ? s.getLogoType() : "image/jpeg";
                data.put("logo", base64);
                data.put("logoType", mimeType);
            }
            sponsorData.add(data);
        }
        model.addAttribute("sponsors", sponsorData);

        // Añadir token CSRF al modelo
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);

        // Añadir flag admin si el usuario es admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("admin", isAdmin);

        return "sponsors";
    }
    

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);
        System.out.println("CSRF Token: " + (csrfToken != null ? csrfToken.getToken() : "NULL"));
        return "login";
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

    @GetMapping("/galeria")
    public String gallery(Model model, HttpServletRequest request) {
        List<Image> images = imageRepository.findAll();
        List<Map<String, String>> imageList = new ArrayList<>();
        for (Image img : images) {
            String base64 = Base64.getEncoder().encodeToString(img.getImageData());
            Map<String, String> map = new HashMap<>();
            map.put("id", img.getId().toString());
            map.put("base64", base64);
            imageList.add(map);
        }
        model.addAttribute("images", imageList);

        // Añadir token CSRF al modelo
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);

        // Añadir flag admin si el usuario es admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("admin", isAdmin);

        return "gallery";
    }
    @GetMapping("/download-image/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String id) {
        Optional<Image> imageOpt = imageRepository.findById(Long.valueOf(id));
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("image_" + id + ".jpg")
                    .build());
            return new ResponseEntity<>(image.getImageData(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}