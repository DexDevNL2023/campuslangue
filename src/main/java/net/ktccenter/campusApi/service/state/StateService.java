package net.ktccenter.campusApi.service.state;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dao.cours.ExamenRepository;
import net.ktccenter.campusApi.dao.scolarite.*;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.*;
import net.ktccenter.campusApi.dto.state.StateReponse;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.scolarite.*;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class StateService extends MainService {
    final ExamenRepository examenRepository;
    final InscriptionRepository inscriptionRepository;
    final SessionRepository sessionRepository;
    final EpreuveRepository epreuveRepository;
    final PaiementRepository paiementRepository;
    final CompteRepository compteRepository;
    final EtudiantRepository etudiantRepository;
    private final CampusRepository campusRepository;

    public StateService(ExamenRepository examenRepository, InscriptionRepository inscriptionRepository, SessionRepository sessionRepository, EpreuveRepository epreuveRepository, PaiementRepository paiementRepository, CompteRepository compteRepository, EtudiantRepository etudiantRepository, CampusRepository campusRepository) {
        this.examenRepository = examenRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.sessionRepository = sessionRepository;
        this.epreuveRepository = epreuveRepository;
        this.paiementRepository = paiementRepository;
        this.compteRepository = compteRepository;
        this.etudiantRepository = etudiantRepository;
        this.campusRepository = campusRepository;
    }

  public StateReponse getAllState() {
      long nbreTotalEtudiant = 0L;
      long nbreTotalAdmis = 0L;
      long nbreTotalEchecs = 0L;
      long nbreTotalEnRattrapages = 0L;
      long nbreTotalScolaritePayee = 0L;
      long nbreTotalScolariteImpayee = 0L;
      long nbreTotalInscription = 0L;
      long nbreTotalSessionOuverte = 0L;
      long nbreTotalSessionSurAnnee = 0L;
      long nbreTotalCampus = 0L;
    
    StateReponse stateResponse = new StateReponse();

      List<StudentBranchDTO> list = getAllStudentByBranch();
      stateResponse.setAllStudentByBranch(list);
      for (StudentBranchDTO student : list) {
          nbreTotalEtudiant = nbreTotalEtudiant + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalEtudiant(nbreTotalEtudiant);

      List<StateEtudiantBranchDTO> admis = getAllEtudiantAdmis();
      stateResponse.setEtudiantAdmis(admis);
      for (StateEtudiantBranchDTO student : admis) {
          nbreTotalAdmis = nbreTotalAdmis + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalAdmis(nbreTotalAdmis);

      List<StateEtudiantBranchDTO> echecs = getAllEtudiantEchecs();
      stateResponse.setEtudiantEchecs(echecs);
      for (StateEtudiantBranchDTO student : echecs) {
          nbreTotalEchecs = nbreTotalEchecs + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalEchecs(nbreTotalEchecs);

      List<StateEtudiantBranchDTO> rattrapages = getAllEtudiantEnRattrapages();
      stateResponse.setEtudiantEnRattrapages(rattrapages);
      for (StateEtudiantBranchDTO student : rattrapages) {
          nbreTotalEnRattrapages = nbreTotalEnRattrapages + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalEnRattrapages(nbreTotalEnRattrapages);

      List<StateEtudiantBranchDTO> payes = getAllEtudiantScolaritePayee();
      stateResponse.setEtudiantScolaritePayee(payes);
      for (StateEtudiantBranchDTO student : payes) {
          nbreTotalScolaritePayee = nbreTotalScolaritePayee + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalScolaritePayee(nbreTotalScolaritePayee);

      List<StateEtudiantBranchDTO> impayes = getAllEtudiantScolariteImpayee();
      stateResponse.setEtudiantScolariteImpayee(impayes);
      for (StateEtudiantBranchDTO student : impayes) {
          nbreTotalScolariteImpayee = nbreTotalScolariteImpayee + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalScolariteImpayee(nbreTotalScolariteImpayee);

      List<StateEtudiantBranchDTO> inscrits = getAllEtudiantInscription();
      stateResponse.setEtudiantInscription(inscrits);
      for (StateEtudiantBranchDTO student : inscrits) {
          nbreTotalInscription = nbreTotalInscription + student.getNbreTotelEtudiant();
      }
      stateResponse.setNbreTotalInscription(nbreTotalInscription);

      List<SessionBranchDTO> sessionOuvertes = getAllSessionOuverte();
      stateResponse.setAllSessionOuverte(sessionOuvertes);
      for (SessionBranchDTO sessionO : sessionOuvertes) {
          nbreTotalSessionOuverte = nbreTotalSessionOuverte + sessionO.getData().size();
      }
      stateResponse.setNbreTotalSessionOuverte(nbreTotalSessionOuverte);

      List<SessionBranchDTO> sessionSurAnnes = getAllSessionSurAnnee();
      stateResponse.setAllSessionSurAnnee(sessionSurAnnes);
      for (SessionBranchDTO sessionA : sessionSurAnnes) {
          nbreTotalSessionSurAnnee = nbreTotalSessionSurAnnee + sessionA.getData().size();
      }
      stateResponse.setNbreTotalSessionSurAnnee(nbreTotalSessionSurAnnee);

      List<CampusBranchDTO> campus = getAllCampusByBranch();
      stateResponse.setAllCampusByBranch(campus);
      for (CampusBranchDTO c : campus) {
          nbreTotalCampus = nbreTotalCampus + c.getData().size();
      }
      stateResponse.setNbreTotalCampus(nbreTotalCampus);
    return stateResponse;
  }

    public List<CampusBranchDTO> getAllCampusByBranch() {
        List<CampusBranchDTO> result = new ArrayList<>();
        for (Branche b : getAllBranches()) {
            result.add(buildCampus(b));
        }
        return result;
    }

    private CampusBranchDTO buildCampus(Branche branche) {
        CampusBranchDTO dto = new CampusBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        dto.setData(campusRepository.findAllByBranche(branche).stream().map(LiteCampusDTO::new).collect(Collectors.toList()));
        return dto;
    }

    public List<SessionBranchDTO> getAllSessionSurAnnee() {
        List<SessionBranchDTO> result = new ArrayList<>();
        for (Branche b : getAllBranches()) {
            result.add(buildSessionSurAnnee(b));
        }
        return result;
    }

    private SessionBranchDTO buildSessionSurAnnee(Branche branche) {
        Date currentDate = MyUtils.currentDate();
        log.info(currentDate.toString());
        SessionBranchDTO dto = new SessionBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        dto.setData(sessionRepository.findAllByBranche(branche)
                .stream()
                .filter(session -> session.getDateDebut().before(currentDate))
                .filter(session -> session.getDateFin().after(currentDate))
                .map(LiteSessionDTO::new).collect(Collectors.toList()));
        return dto;
    }

    public List<SessionBranchDTO> getAllSessionOuverte() {
        List<SessionBranchDTO> result = new ArrayList<>();
        for (Branche b : getAllBranches()) {
            result.add(buildSessionOuverte(b));
        }
        return result;
    }

    private SessionBranchDTO buildSessionOuverte(Branche branche) {
        SessionBranchDTO dto = new SessionBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        dto.setData(sessionRepository.findAllByBranche(branche).stream().filter(session -> !session.getEstTerminee()).map(LiteSessionDTO::new).collect(Collectors.toList()));
        return dto;
    }

    public List<StudentBranchDTO> getAllStudentByBranch() {
    List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findAll();
    List<StudentBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, etudiants));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), etudiants));
    }
    return result;
  }

    StudentBranchDTO buildData(Branche branche, List<Etudiant> etudiants) {
    List<LiteEtudiantForStateDTO> data = etudiants.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(LiteEtudiantForStateDTO::new)
            .collect(Collectors.toList());
    StudentBranchDTO dto = new StudentBranchDTO();
    dto.setBranche(new LiteBrancheForStateDTO(branche));
    dto.setData(data);
    return dto;
  }

    boolean belongsToTheCurrentBranch(Branche branche, Etudiant e) {
    return e.getBranche().getId().equals(branche.getId());
  }

    public List<StateEtudiantBranchDTO> getAllEtudiantScolariteImpayee() {
    List<StateEtudiantBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
          result.add(buildEtudiantScolariteUnpaid(b, true));
      }
    } else {
        result.add(buildEtudiantScolariteUnpaid(getCurrentUserBranch(), true));
    }
    return result;
  }

    public List<StateEtudiantBranchDTO> getAllEtudiantScolaritePayee() {
    List<StateEtudiantBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
          result.add(buildEtudiantScolariteUnpaid(b, false));
      }
    } else {
        result.add(buildEtudiantScolariteUnpaid(getCurrentUserBranch(), false));
    }
    return result;
  }

    StateEtudiantBranchDTO buildEtudiantScolariteUnpaid(Branche branche, Boolean haveUnpaid) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(branche);
    for (Session session : sessions) {
      StateEtudiantSessionDTO stateEtudiantSessionDTO = new StateEtudiantSessionDTO();
      LiteSessionForStateDTO liteSessionForStateDTO = new LiteSessionForStateDTO(session);
      liteSessionForStateDTO.setNiveau(new LiteNiveauForStateDTO(session.getNiveau()));
      stateEtudiantSessionDTO.setSession(liteSessionForStateDTO);
      List<LiteEtudiantForStateDTO> dataStateEtudiantSessionDTO = new ArrayList<>();
      List<Inscription> inscriptions = inscriptionRepository.findAllBySession(session);
      for (Inscription inscription : inscriptions) {
          if (haveUnpaid(inscription, session.getNiveau()) == haveUnpaid) {
              dataStateEtudiantSessionDTO.add(new LiteEtudiantForStateDTO(inscription.getEtudiant()));
          }
      }
      dataStateEtudiantBranchDTO.add(stateEtudiantSessionDTO);
    }
    stateEtudiantBranchDTO.setData(dataStateEtudiantBranchDTO);
    return stateEtudiantBranchDTO;
  }

    boolean haveUnpaid(Inscription inscription, Niveau niveau) {
        BigDecimal unpaid = BigDecimal.valueOf(0.0);
        BigDecimal solde = BigDecimal.valueOf(0.0);
        BigDecimal netApayer = niveau.getFraisPension().add(niveau.getFraisInscription());
        Compte compte = compteRepository.findByInscription(inscription);
        List<Paiement> paiements = paiementRepository.findAllByCompte(compte);
        for (Paiement paiement : paiements) {
            solde = solde.add(paiement.getMontant());
        }
        unpaid = netApayer.subtract(solde);
        return unpaid.intValue() > 0;
  }

    public List<StateEtudiantBranchDTO> getAllEtudiantEnRattrapages() {
    List<StateEtudiantBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildEtudiantEnRattrapage(b));
      }
    } else {
      result.add(buildEtudiantEnRattrapage(getCurrentUserBranch()));
    }
    return result;
  }

    StateEtudiantBranchDTO buildEtudiantEnRattrapage(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(branche);
    for (Session session : sessions) {
      StateEtudiantSessionDTO stateEtudiantSessionDTO = new StateEtudiantSessionDTO();
      LiteSessionForStateDTO liteSessionForStateDTO = new LiteSessionForStateDTO(session);
      liteSessionForStateDTO.setNiveau(new LiteNiveauForStateDTO(session.getNiveau()));
      stateEtudiantSessionDTO.setSession(liteSessionForStateDTO);
      List<LiteEtudiantForStateDTO> dataStateEtudiantSessionDTO = new ArrayList<>();
      List<Inscription> inscriptions = inscriptionRepository.findAllBySession(session);
      for (Inscription inscription : inscriptions) {
        if (isRattrapage(inscription))
          dataStateEtudiantSessionDTO.add(new LiteEtudiantForStateDTO(inscription.getEtudiant()));
      }
      dataStateEtudiantBranchDTO.add(stateEtudiantSessionDTO);
    }
    stateEtudiantBranchDTO.setData(dataStateEtudiantBranchDTO);
    return stateEtudiantBranchDTO;
  }

    boolean isRattrapage(Inscription inscription) {
        boolean isRattrapage = false;
    Examen examen = examenRepository.findByInscription(inscription);
    List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
    for (Epreuve epreuve : epreuves) {
        if (epreuve.getEstRattrapee()) {
            isRattrapage = true;
            break;
        }
    }
        return isRattrapage;
  }

    public List<StateEtudiantBranchDTO> getAllEtudiantAdmis() {
    List<StateEtudiantBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildEtudiantAdmis(b));
      }
    } else {
      result.add(buildEtudiantAdmis(getCurrentUserBranch()));
    }
    return result;
  }

    StateEtudiantBranchDTO buildEtudiantAdmis(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(branche);
    for (Session session : sessions) {
      StateEtudiantSessionDTO stateEtudiantSessionDTO = new StateEtudiantSessionDTO();
      LiteSessionForStateDTO liteSessionForStateDTO = new LiteSessionForStateDTO(session);
      liteSessionForStateDTO.setNiveau(new LiteNiveauForStateDTO(session.getNiveau()));
      stateEtudiantSessionDTO.setSession(liteSessionForStateDTO);
      List<LiteEtudiantForStateDTO> dataStateEtudiantSessionDTO = new ArrayList<>();
      List<Inscription> inscriptions = inscriptionRepository.findAllBySession(session);
      for (Inscription inscription : inscriptions) {
        if (hasWin(inscription))
          dataStateEtudiantSessionDTO.add(new LiteEtudiantForStateDTO(inscription.getEtudiant()));
      }
      dataStateEtudiantBranchDTO.add(stateEtudiantSessionDTO);
    }
    stateEtudiantBranchDTO.setData(dataStateEtudiantBranchDTO);
    return stateEtudiantBranchDTO;
  }

    public List<StateEtudiantBranchDTO> getAllEtudiantEchecs() {
    List<StateEtudiantBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildEtudiantEchec(b));
      }
    } else {
      result.add(buildEtudiantEchec(getCurrentUserBranch()));
    }
    return result;
  }

    StateEtudiantBranchDTO buildEtudiantEchec(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(branche);
    for (Session session : sessions) {
      StateEtudiantSessionDTO stateEtudiantSessionDTO = new StateEtudiantSessionDTO();
      LiteSessionForStateDTO liteSessionForStateDTO = new LiteSessionForStateDTO(session);
      liteSessionForStateDTO.setNiveau(new LiteNiveauForStateDTO(session.getNiveau()));
      stateEtudiantSessionDTO.setSession(liteSessionForStateDTO);
      List<LiteEtudiantForStateDTO> dataStateEtudiantSessionDTO = new ArrayList<>();
      List<Inscription> inscriptions = inscriptionRepository.findAllBySession(session);
      for (Inscription inscription : inscriptions) {
        if (!hasWin(inscription))
          dataStateEtudiantSessionDTO.add(new LiteEtudiantForStateDTO(inscription.getEtudiant()));
      }
      dataStateEtudiantBranchDTO.add(stateEtudiantSessionDTO);
    }
    stateEtudiantBranchDTO.setData(dataStateEtudiantBranchDTO);
    return stateEtudiantBranchDTO;
  }

    boolean hasWin(Inscription inscription) {
    Float totalNote = 0F;
        Float moyenne = 0F;
    boolean hisNotValid = false;
    Examen examen = examenRepository.findByInscription(inscription);
    List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
    for (Epreuve epreuve : epreuves) {
        if (epreuve.getEstValidee()) {
            totalNote = epreuve.getNoteObtenue();
        } else {
            hisNotValid = true;
        }
    }
        if (hisNotValid) {
            moyenne = 0F;
        } else {
            if (epreuves.isEmpty()) {
                moyenne = 0F;
            } else {
                moyenne = totalNote / epreuves.size();
            }
    }
        return moyenne.intValue() >= 10;
  }

  public List<StateEtudiantBranchDTO> getAllEtudiantInscription() {
    List<StateEtudiantBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildEtudiantInscription(b));
      }
    } else {
      result.add(buildEtudiantInscription(getCurrentUserBranch()));
    }
    return result;
  }

    StateEtudiantBranchDTO buildEtudiantInscription(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(branche);
    for (Session session : sessions) {
      StateEtudiantSessionDTO stateEtudiantSessionDTO = new StateEtudiantSessionDTO();
      LiteSessionForStateDTO liteSessionForStateDTO = new LiteSessionForStateDTO(session);
      liteSessionForStateDTO.setNiveau(new LiteNiveauForStateDTO(session.getNiveau()));
      stateEtudiantSessionDTO.setSession(liteSessionForStateDTO);
      List<LiteEtudiantForStateDTO> dataStateEtudiantSessionDTO = new ArrayList<>();
      List<Inscription> inscriptions = inscriptionRepository.findAllBySession(session);
      for (Inscription inscription : inscriptions) {
        dataStateEtudiantSessionDTO.add(new LiteEtudiantForStateDTO(inscription.getEtudiant()));
      }
      dataStateEtudiantBranchDTO.add(stateEtudiantSessionDTO);
    }
    stateEtudiantBranchDTO.setData(dataStateEtudiantBranchDTO);
    return stateEtudiantBranchDTO;
  }
}
