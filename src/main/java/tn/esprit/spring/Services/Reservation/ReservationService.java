package tn.esprit.spring.Services.Reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {
    ReservationRepository repo;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;

    @Override
    public Reservation addOrUpdate(Reservation r) {
        log.info("Adding/Updating reservation: {}", r.getIdReservation());
        return repo.save(r);
    }

    @Override
    public List<Reservation> findAll() {
        log.info("Fetching all reservations");
        return repo.findAll();
    }

    @Override
    public Reservation findById(String id) {
        log.info("Fetching reservation with id: {}", id);
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting reservation with id: {}", id);
        repo.deleteById(id);
    }

    @Override
    public void delete(Reservation r) {
        log.info("Deleting reservation: {}", r.getIdReservation());
        repo.delete(r);
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Long numChambre, long cin) {
        log.info("Starting reservation process for room number: {} and student CIN: {}", numChambre, cin);

        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int numReservation;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        Reservation res = new Reservation();
        Chambre c = chambreRepository.findByNumeroChambre(numChambre);
        log.debug("Found chamber: {}", c.getIdChambre());

        Etudiant e = etudiantRepository.findByCin(cin);
        log.debug("Found student: {}", e.getCin());

        boolean ajout = false;
        int numRes = chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(c.getIdChambre(), dateDebutAU, dateFinAU);
        log.debug("Current reservations count for room: {}", numRes);
        System.err.println(numRes);
        switch (c.getTypeC()) {
            case SIMPLE:
                if (numRes < 1) {
                    ajout = true;
                } else {
                    log.info("Chambre simple remplie !");
                }
                break;
            case DOUBLE:
                if (numRes < 2) {
                    ajout = true;
                } else {
                    log.info("Chambre double remplie !");
                }
                break;
            case TRIPLE:
                if (numRes < 3) {
                    ajout = true;
                } else {
                    log.info("Chambre triple remplie !");
                }
                break;
        }
        if (ajout) {
            log.info("Creating new reservation for room {} and student {}", numChambre, cin);
            res.setEstValide(false);
            res.setAnneeUniversitaire(LocalDate.now());
            res.setIdReservation(dateDebutAU.getYear() + "/" + dateFinAU.getYear() + "-" + c.getBloc().getNomBloc() + "-" + c.getNumeroChambre() + "-" + e.getCin());
            res.getEtudiants().add(e);
            res.setEstValide(true);
            res = repo.save(res);
            c.getReservations().add(res);
            chambreRepository.save(c);
        } else {
            log.warn("Unable to create reservation - room capacity reached");
        }

        return res;
    }

    @Override
    public long getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee) {
        log.info("Counting reservations between {} and {}", debutAnnee, finAnnee);
        return repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Override
    public String annulerReservation(long cinEtudiant) {
        log.info("Cancelling reservation for student CIN: {}", cinEtudiant);
        Reservation r = repo.findByEtudiantsCinAndEstValide(cinEtudiant, true);
        Chambre c = chambreRepository.findByReservationsIdReservation(r.getIdReservation());
        c.getReservations().remove(r);
        chambreRepository.save(c);
        repo.delete(r);
        log.info("Successfully cancelled reservation: {}", r.getIdReservation());
        return "La réservation " + r.getIdReservation() + " est annulée avec succés";
    }

    @Override
    public void affectReservationAChambre(String idRes, long idChambre) {
        log.info("Assigning reservation {} to room {}", idRes, idChambre);
        Reservation r = repo.findById(idRes).get();
        Chambre c = chambreRepository.findById(idChambre).get();
        c.getReservations().add(r);
        chambreRepository.save(c);
        log.info("Successfully assigned reservation to room");
    }

    @Override
    public void annulerReservations() {
        log.info("Starting batch cancellation of reservations");
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int numReservation;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        for (Reservation reservation : repo.findByEstValideAndAnneeUniversitaireBetween(true, dateDebutAU, dateFinAU)) {
            log.debug("Cancelling reservation: {}", reservation.getIdReservation());
            reservation.setEstValide(false);
            repo.save(reservation);
        }
        log.info("Completed batch cancellation of reservations");
    }
}
