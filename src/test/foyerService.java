package tn.esprit.spring.Services.Foyer;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {

    private static final Logger logger = LoggerFactory.getLogger(FoyerService.class);
    
    FoyerRepository repo;
    UniversiteRepository universiteRepository;
    BlocRepository blocRepository;

    @Override
    public Foyer addOrUpdate(Foyer f) {
        logger.info("Ajout ou mise à jour du Foyer avec l'ID : {}", f.getId());
        Foyer savedFoyer = repo.save(f);
        logger.info("Foyer ajouté/mis à jour avec succès, ID : {}", savedFoyer.getId());
        return savedFoyer;
    }

    @Override
    public List<Foyer> findAll() {
        logger.info("Récupération de tous les foyers");
        List<Foyer> foyers = repo.findAll();
        logger.info("Nombre de foyers récupérés : {}", foyers.size());
        return foyers;
    }

    @Override
    public Foyer findById(long id) {
        logger.info("Recherche du Foyer avec l'ID : {}", id);
        Foyer foyer = repo.findById(id).orElse(null);
        if (foyer != null) {
            logger.info("Foyer avec l'ID : {} trouvé", id);
        } else {
            logger.warn("Foyer avec l'ID : {} non trouvé", id);
        }
        return foyer;
    }

    @Override
    public void deleteById(long id) {
        logger.info("Suppression du Foyer avec l'ID : {}", id);
        repo.deleteById(id);
        logger.info("Foyer avec l'ID : {} supprimé avec succès", id);
    }

    @Override
    public void delete(Foyer f) {
        logger.info("Suppression du Foyer : {}", f);
        repo.delete(f);
        logger.info("Foyer supprimé avec succès : {}", f);
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        logger.info("Affectation du Foyer avec l'ID : {} à l'Université nommée : {}", idFoyer, nomUniversite);
        Foyer f = findById(idFoyer);
        Universite u = universiteRepository.findByNomUniversite(nomUniversite);
        if (f != null && u != null) {
            u.setFoyer(f);
            Universite updatedUniversite = universiteRepository.save(u);
            logger.info("Foyer avec l'ID : {} affecté avec succès à l'Université : {}", idFoyer, nomUniversite);
            return updatedUniversite;
        } else {
            logger.warn("Foyer avec l'ID : {} ou Université nommée : {} non trouvée", idFoyer, nomUniversite);
            return null;
        }
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        logger.info("Suppression de l'affectation du Foyer de l'Université avec l'ID : {}", idUniversite);
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        if (u != null) {
            u.setFoyer(null);
            Universite updatedUniversite = universiteRepository.save(u);
            logger.info("Foyer supprimé de l'Université avec l'ID : {}", idUniversite);
            return updatedUniversite;
        } else {
            logger.warn("Université avec l'ID : {} non trouvée", idUniversite);
            return null;
        }
    }

    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        logger.info("Ajout du Foyer et affectation à l'Université avec l'ID : {}", idUniversite);
        List<Bloc> blocs = foyer.getBlocs();
        Foyer savedFoyer = repo.save(foyer);
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        if (u != null) {
            for (Bloc bloc : blocs) {
                bloc.setFoyer(savedFoyer);
                blocRepository.save(bloc);
            }
            u.setFoyer(savedFoyer);
            Universite updatedUniversite = universiteRepository.save(u);
            logger.info("Foyer avec l'ID : {} affecté à l'Université avec l'ID : {}", savedFoyer.getId(), idUniversite);
            return updatedUniversite.getFoyer();
        } else {
            logger.warn("Université avec l'ID : {} non trouvée, Foyer non affecté", idUniversite);
            return null;
        }
    }

    @Override
    public Foyer ajoutFoyerEtBlocs(Foyer foyer) {
        logger.info("Ajout du Foyer et de ses blocs associés");
        List<Bloc> blocs = foyer.getBlocs();
        Foyer savedFoyer = repo.save(foyer);
        for (Bloc bloc : blocs) {
            bloc.setFoyer(savedFoyer);
            blocRepository.save(bloc);
        }
        logger.info("Foyer avec l'ID : {} et ses blocs associés ajoutés avec succès", savedFoyer.getId());
        return savedFoyer;
    }
}
