package tn.esprit.spring.Restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.Services.Bloc.IBlocService;

import lombok.extern.slf4j.Slf4j;


import java.util.List;

@RestController
@RequestMapping("bloc")
@AllArgsConstructor
@Slf4j
public class BlocRestController {
    IBlocService service;

    @PostMapping("addOrUpdate")
    public Bloc addOrUpdate(@RequestBody Bloc b) {
        log.info("Received request to add or update bloc: {}", b);
        return service.addOrUpdate(b);
    }

    @GetMapping("findAll")
    public List<Bloc> findAll() {
        log.info("Received request to find all Blocs");
        return service.findAll();
    }

    @GetMapping("findById")
    public Bloc findById(@RequestParam long id) {
        log.info("Received request to find Bloc by ID: {}", id);
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Bloc b) {
        log.warn("Received request to delete Bloc: {}", b);
        service.delete(b);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        log.warn("Received request to delete Bloc by ID: {}", id);
        service.deleteById(id);
    }

    @PutMapping("affecterChambresABloc")
    public Bloc affecterChambresABloc(@RequestBody List<Long> numChambre, @RequestParam String nomBloc) {
        log.info("Received request to assign Chambres to Bloc with name: {}", nomBloc);
        return service.affecterChambresABloc(numChambre, nomBloc);
    }

    @PutMapping("affecterBlocAFoyer")
    public Bloc affecterBlocAFoyer(@RequestParam String nomBloc,@RequestParam String nomFoyer){
        log.info("Received request to assign Bloc: {} to Foyer: {}", nomBloc, nomFoyer);
        return service.affecterBlocAFoyer(nomBloc,nomFoyer);
    }
}
