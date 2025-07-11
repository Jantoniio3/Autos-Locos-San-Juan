package com.autoslocos.autoslocos.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.autoslocos.autoslocos.Entity.*;
import com.autoslocos.autoslocos.Repository.*;

@Service
public class NewnessService {
    @Autowired
    private NewnessRepository newnessRepository;

    public Newness addNewness(LocalDate date, String title, String description, MultipartFile file) throws IOException {
        Newness newness = new Newness(date, title, description);
        if (file != null && !file.isEmpty()) {
            newness.setFileInfo(file);
        }
        return newnessRepository.save(newness);
    }
    
    public List<Newness> getAllNewness() {
        return newnessRepository.findAll();
    }
    
    public void deleteNewness(Long id) {
        newnessRepository.deleteById(id);
    }

    public Newness saveNewness(Newness newness) {
        return newnessRepository.save(newness);
    }
}
