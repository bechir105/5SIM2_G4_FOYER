package tn.esprit.spring.Services.Bloc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService {

    private static final Logger log = LogManager.getLogger(BlocService.class);

    BlocRepository repo;
    ChambreRepository chambreRepository;
    BlocRepository blocRepository;
    FoyerRepository foyerRepository;

    @Override
    public Bloc addOrUpdate2(Bloc b) { //Cascade
        log.info("Adding or updating Bloc with Cascade: {}", b);
        List<Chambre> chambres= b.getChambres();
        for (Chambre c: chambres) {
            c.setBloc(b);
            chambreRepository.save(c);
        }
        log.info("Updated Bloc: {}", b);
        return b;
    }

    @Override
    public Bloc addOrUpdate(Bloc b) {
        log.info("Adding or updating Bloc: {}", b);
        List<Chambre> chambres= b.getChambres();
        b= repo.save(b);
        for (Chambre chambre: chambres) {
            chambre.setBloc(b);
            chambreRepository.save(chambre);
        }
        log.info("Updated Bloc: {}", b);
        return b;
    }

    @Override
    public List<Bloc> findAll() {
        log.info("Fetching all Blocs");
        return repo.findAll();
    }

    @Override
    public Bloc findById(long id) {
        log.info("Finding Bloc with ID: {}", id);
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        log.warn("Deleting Bloc with ID: {}", id);
        repo.deleteById(id);
    }

    @Override
    public void delete(Bloc b) {
        log.warn("Deleting Bloc: {}", b);
        List<Chambre> chambres= b.getChambres();
        for (Chambre chambre: chambres) {
            chambreRepository.delete(chambre);
        }
        repo.delete(b);
        log.warn("Deleted Bloc: {}", b);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        log.info("Assigning rooms to Bloc with name: {}", nomBloc);
        //1
        Bloc b = repo.findByNomBloc(nomBloc);
        List<Chambre> chambres= new ArrayList<>();
        for (Long nu: numChambre) {
            Chambre chambre=chambreRepository.findByNumeroChambre(nu);
            chambres.add(chambre);
        }
        // Keyword (2ème méthode)
        //chambres=chambreRepository.findAllByNumeroChambre(numChambre);
        //2 Parent==>Chambre  Child==> Bloc
        for (Chambre cha : chambres) {
            //3 On affecte le child au parent
                cha.setBloc(b);
            //4 save du parent
                chambreRepository.save(cha);
        }
        log.info("Assigned rooms to Bloc: {}", b);
        return b;
    }

    @Override
    public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
        log.info("Assigning Bloc with name {} to Foyer with name {}", nomBloc, nomFoyer);
        Bloc b = blocRepository.findByNomBloc(nomBloc); //Parent
        Foyer f = foyerRepository.findByNomFoyer(nomFoyer); //Child
        //On affecte le child au parent
        b.setFoyer(f);
        log.info("Assigned Bloc to Foyer: {}", blocRepository.save(b));
        return blocRepository.save(b);
    }
}
