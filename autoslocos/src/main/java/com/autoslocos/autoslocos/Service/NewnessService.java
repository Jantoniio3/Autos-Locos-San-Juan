package com.autoslocos.autoslocos.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.autoslocos.autoslocos.Entity.*;
import com.autoslocos.autoslocos.Repository.*;

@Service
public class NewnessService {
    @Autowired
    private NewnessRepository newnessRepository;

    public Newness addNewness(LocalDate date, String title, String description, MultipartFile image, MultipartFile file) throws IOException {
        Newness newness = new Newness(date, title, description);

        // Guardar imagen si se adjunta
        if (image != null && !image.isEmpty()) {
            newness.setImageInfo(image); // Debes tener este método en tu entidad
        }

        // Guardar documento si se adjunta
        if (file != null && !file.isEmpty()) {
            newness.setFileInfo(file); // Debes tener este método en tu entidad
        }

        return newnessRepository.save(newness);
    }

    public List<Newness> getAllNewness() {
        return newnessRepository.findAllByOrderByDateDesc();
    }

    public void deleteNewness(Long id) {
        newnessRepository.deleteById(id);
    }

    public Newness saveNewness(Newness newness) {
        return newnessRepository.save(newness);
    }

    public Newness findById(Long id) {
        Optional<Newness> optional = newnessRepository.findById(id);
        return optional.orElse(null);
    }
}
