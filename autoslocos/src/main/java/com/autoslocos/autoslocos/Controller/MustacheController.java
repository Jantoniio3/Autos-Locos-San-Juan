package com.autoslocos.autoslocos.Controller;

import com.autoslocos.autoslocos.Entity.Vehicle;
import com.autoslocos.autoslocos.Service.VehicleService;
import com.autoslocos.autoslocos.Service.SponsorService;
import com.autoslocos.autoslocos.Entity.Sponsor;
import com.autoslocos.autoslocos.Entity.Image;
import com.autoslocos.autoslocos.Repository.ImageRepository;
import com.autoslocos.autoslocos.Entity.Newness;
import com.autoslocos.autoslocos.Service.NewnessService;
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
    private final NewnessService newnessService;

    public MustacheController(VehicleService vehicleService, SponsorService sponsorService, ImageRepository imageRepository, NewnessService newnessService) {
        this.vehicleService = vehicleService;
        this.sponsorService = sponsorService;
        this.imageRepository = imageRepository;
        this.newnessService = newnessService;
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
    public String newness(Model model, HttpServletRequest request) {
        List<Newness> newnessList = newnessService.getAllNewness();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Newness n : newnessList) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", n.getId());
            data.put("title", n.getTitle());
            data.put("date", n.getDate());
            String desc = n.getDescription();
            data.put("descriptionShort", desc != null && desc.length() > 80 ? desc.substring(0, 80) + "..." : desc);
            result.add(data);
        }
        model.addAttribute("newnessList", result);

        // Añade el token CSRF al modelo
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);

        // Añade el flag admin si el usuario es admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("admin", isAdmin);

        return "newness";
    }

    @GetMapping("/novedades/{id}")
    public String newnessDetails(@PathVariable Long id, Model model, HttpServletRequest request) {
        Newness newness = newnessService.findById(id);
        if (newness == null) {
            return "redirect:/novedades";
        }

        // Pasar los datos de la novedad al modelo
        model.addAttribute("id", newness.getId());
        model.addAttribute("title", newness.getTitle());
        model.addAttribute("date", newness.getDate());
        model.addAttribute("description", newness.getDescription());

        // Imagen (si existe)
        if (newness.getImageData() != null && newness.getImageData().length > 0) {
            model.addAttribute("imageData", Base64.getEncoder().encodeToString(newness.getImageData()));
            model.addAttribute("imageType", newness.getImageType());
            model.addAttribute("imageName", newness.getImageName());
        }

        // Documento (si existe)
        if (newness.getFileData() != null && newness.getFileData().length > 0) {
            model.addAttribute("fileData", true); // Solo para mostrar el bloque
            model.addAttribute("fileName", newness.getFileName());
            model.addAttribute("fileType", newness.getFileType());
            model.addAttribute("fileSize", newness.getFileSize());
        }

        // Añadir token CSRF al modelo
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);

        // Añadir flag admin si el usuario es admin
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("admin", isAdmin);

        return "newnessdetails";
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

    @PostMapping("/aceptar-cookies")
    public String aceptarCookies(HttpServletRequest request, @RequestHeader(value = "Referer", required = false) String referer) {
        request.getSession(true).setAttribute("cookiesAccepted", true);
        // Redirige a la página anterior o al inicio
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @GetMapping("/politica-cookies")
    public String politicaCookies(Model model, HttpServletRequest request) {

        return "cookies";
    }

    @GetMapping("/novedades/{id}/descargar-archivo")
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long id) {
        Newness newness = newnessService.findById(id);
        if (newness == null || newness.getFileData() == null || newness.getFileData().length == 0) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(newness.getFileName()).build());
        headers.setContentType(MediaType.parseMediaType(newness.getFileType()));
        headers.setContentLength(newness.getFileData().length);
        return new ResponseEntity<>(newness.getFileData(), headers, HttpStatus.OK);
    }
}