package tn.esprit.spring.RestControllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Universite.IUniversiteService;
import java.util.List;

@RestController
@RequestMapping("universite")
@AllArgsConstructor
@Slf4j
public class UniversiteRestController {
    IUniversiteService service;

    @PostMapping("addOrUpdate")
    public Universite addOrUpdate(@RequestBody Universite u) {
        log.info("Received request to add or update Universite: {}", u);
        return service.addOrUpdate(u);
    }

    @GetMapping("findAll")
    public List<Universite> findAll() {
        log.info("Received request to find all Universites");
        return service.findAll();
    }

    @GetMapping("findById")
    public Universite findById(@RequestParam long id) {
        log.info("Received request to find Universite by ID: {}", id);
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Universite u) {
        log.warn("Received request to delete Universite: {}", u);
        service.delete(u);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        log.warn("Received request to delete Universite by ID: {}", id);
        service.deleteById(id);
    }
}
