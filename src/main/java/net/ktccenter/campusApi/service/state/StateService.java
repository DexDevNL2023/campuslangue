package net.ktccenter.campusApi.service.state;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dao.cours.ExamenRepository;
import net.ktccenter.campusApi.dao.scolarite.*;
import net.ktccenter.campusApi.dto.reponse.branch.*;
import net.ktccenter.campusApi.dto.state.StateReponse;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.scolarite.*;
import net.ktccenter.campusApi.service.MainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class StateService extends MainService {
  private final ExamenRepository examenRepository;
  private final InscriptionRepository inscriptionRepository;
  private final SessionRepository sessionRepository;
  private final EpreuveRepository epreuveRepository;
  private final PaiementRepository paiementRepository;
  private final CompteRepository compteRepository;
  private final EtudiantRepository etudiantRepository;

  public StateService(ExamenRepository examenRepository, InscriptionRepository inscriptionRepository, SessionRepository sessionRepository, EpreuveRepository epreuveRepository, PaiementRepository paiementRepository, CompteRepository compteRepository, EtudiantRepository etudiantRepository) {
    this.examenRepository = examenRepository;
    this.inscriptionRepository = inscriptionRepository;
    this.sessionRepository = sessionRepository;
    this.epreuveRepository = epreuveRepository;
    this.paiementRepository = paiementRepository;
    this.compteRepository = compteRepository;
    this.etudiantRepository = etudiantRepository;
  }

  public StateReponse getAllState() {
    StateReponse stateResponse = new StateReponse();
    stateResponse.setAllStudentByBranch(getAllEtudiantBySession());
    stateResponse.setEtudiantEchecs(getAllEtudiantEchecs());
    stateResponse.setEtudiantAdmis(getAllEtudiantAdmis());
    stateResponse.setEtudiantEnRattrapages(getAllEtudiantEnRattrapages());
    stateResponse.setEtudiantScolaritePayee(getAllEtudiantScolaritePayee());
    stateResponse.setEtudiantScolariteImpayee(getAllEtudiantScolariteImpayee());
    stateResponse.setEtudiantInscription(getAllEtudiantInscription());
    return stateResponse;
  }

  private List<StudentBranchDTO> getAllEtudiantBySession() {
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

  private StudentBranchDTO buildData(Branche branche, List<Etudiant> etudiants) {
    List<LiteEtudiantForStateDTO> data = etudiants.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(LiteEtudiantForStateDTO::new)
            .collect(Collectors.toList());
    StudentBranchDTO dto = new StudentBranchDTO();
    dto.setBranche(new LiteBrancheForStateDTO(branche));
    dto.setData(data);
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, Etudiant e) {
    return e.getBranche().getId().equals(branche.getId());
  }

  private List<StateEtudiantBranchDTO> getAllEtudiantScolariteImpayee() {
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

  private List<StateEtudiantBranchDTO> getAllEtudiantScolaritePayee() {
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

  private StateEtudiantBranchDTO buildEtudiantScolariteUnpaid(Branche branche, Boolean haveUnpaid) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(getCurrentUserBranch());
    for (Session session : sessions) {
      StateEtudiantSessionDTO stateEtudiantSessionDTO = new StateEtudiantSessionDTO();
      LiteSessionForStateDTO liteSessionForStateDTO = new LiteSessionForStateDTO(session);
      liteSessionForStateDTO.setNiveau(new LiteNiveauForStateDTO(session.getNiveau()));
      stateEtudiantSessionDTO.setSession(liteSessionForStateDTO);
      List<LiteEtudiantForStateDTO> dataStateEtudiantSessionDTO = new ArrayList<>();
      List<Inscription> inscriptions = inscriptionRepository.findAllBySession(session);
      for (Inscription inscription : inscriptions) {
        if (haveUnpaid(inscription) == haveUnpaid)
          dataStateEtudiantSessionDTO.add(new LiteEtudiantForStateDTO(inscription.getEtudiant()));
      }
      dataStateEtudiantBranchDTO.add(stateEtudiantSessionDTO);
    }
    stateEtudiantBranchDTO.setData(dataStateEtudiantBranchDTO);
    return stateEtudiantBranchDTO;
  }

  private boolean haveUnpaid(Inscription inscription) {
    CalculTotals calcul = calculSolde(inscription);
      return calcul.getSolde().compareTo(calcul.getResteApayer()) != 0;
  }

  private CalculTotals calculSolde(Inscription inscription) {
    Compte compte = compteRepository.findByInscription(inscription);
    List<Paiement> paiements = paiementRepository.findAllByCompte(compte);
    CalculTotals calcul = new CalculTotals();
    BigDecimal solde = BigDecimal.valueOf(0.0);
    BigDecimal reste = BigDecimal.valueOf(0.0);
    for (Paiement paiement : paiements) {
      BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
      solde = solde.add(paiement.getMontant());
      reste = netApayer.subtract(solde);
    }
    calcul.setSolde(solde);
    calcul.setResteApayer(reste);
    return calcul;
  }

  private List<StateEtudiantBranchDTO> getAllEtudiantEnRattrapages() {
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

  private StateEtudiantBranchDTO buildEtudiantEnRattrapage(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(getCurrentUserBranch());
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

  private boolean isRattrapage(Inscription inscription) {
    Examen examen = examenRepository.findByInscription(inscription);
    List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
    for (Epreuve epreuve : epreuves) {
      if (!epreuve.getEstRattrapee()) return true;
    }
    return false;
  }

  private List<StateEtudiantBranchDTO> getAllEtudiantAdmis() {
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

  private StateEtudiantBranchDTO buildEtudiantAdmis(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(getCurrentUserBranch());
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

  private List<StateEtudiantBranchDTO> getAllEtudiantEchecs() {
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

  private StateEtudiantBranchDTO buildEtudiantEchec(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(getCurrentUserBranch());
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

  private boolean hasWin(Inscription inscription) {
    Float totalNote = 0F;
    float moyenne = 0F;
    boolean hisNotValid = false;
    Examen examen = examenRepository.findByInscription(inscription);
    List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
    for (Epreuve epreuve : epreuves) {
      if (!epreuve.getEstValidee()) hisNotValid = true;
      totalNote += epreuve.getNoteObtenue();
    }
    if (!epreuves.isEmpty() || !hisNotValid) {
      moyenne = totalNote / epreuves.size();
    }
    return (moyenne >= 10);
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

  private StateEtudiantBranchDTO buildEtudiantInscription(Branche branche) {
    StateEtudiantBranchDTO stateEtudiantBranchDTO = new StateEtudiantBranchDTO();
    stateEtudiantBranchDTO.setBranche(new LiteBrancheForStateDTO(branche));
    List<StateEtudiantSessionDTO> dataStateEtudiantBranchDTO = new ArrayList<>();
    List<Session> sessions = sessionRepository.findAllByBranche(getCurrentUserBranch());
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
