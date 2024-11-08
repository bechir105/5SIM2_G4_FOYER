package tn.esprit.spring.Services.Universite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UniversiteService implements IUniversiteService {
    UniversiteRepository repo;

    @Override
    public Universite addOrUpdate(Universite u) {
        log.info("Adding or updating Universite: {}", u);
        return repo.save(u);
    }

    @Override
    public List<Universite> findAll() {
        log.info("Fetching all Universites");
        return repo.findAll();
    }

    @Override
    public Universite findById(long id) {
        log.info("Finding Universite with ID: {}", id);
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        log.warn("Deleting Universite with ID: {}", id);
        repo.deleteById(id);
    }

    @Override
    public void delete(Universite u) {
        log.warn("Deleting Universite: {}", u);
        repo.delete(u);
    }
}
