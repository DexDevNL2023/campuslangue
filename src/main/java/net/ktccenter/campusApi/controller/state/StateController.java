package net.ktccenter.campusApi.controller.state;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dto.reponse.branch.CampusBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SessionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.StateEtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.StudentBranchDTO;
import net.ktccenter.campusApi.dto.state.StateReponse;
import net.ktccenter.campusApi.service.state.StateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/state")
@RestController
@Slf4j
@CrossOrigin("*")
public class StateController {
  
  private final StateService stateService;

  public StateController(StateService stateService) {
      this.stateService = stateService;
  }

  @GetMapping
  public StateReponse getAllState() {
    return stateService.getAllState();
  }

  @GetMapping("/student/by/branch")
  public List<StudentBranchDTO> getAllStudentByBranch() {
    return stateService.getAllStudentByBranch();
  }

  @GetMapping("/student/fail")
  public List<StateEtudiantBranchDTO> getAllEtudiantEchecs() {
    return stateService.getAllEtudiantEchecs();
  }

  @GetMapping("/student/admitted")
  public List<StateEtudiantBranchDTO> getAllEtudiantAdmis() {
    return stateService.getAllEtudiantAdmis();
  }

  @GetMapping("/student/catch-up")
  public List<StateEtudiantBranchDTO> getAllEtudiantEnRattrapages() {
    return stateService.getAllEtudiantEnRattrapages();
  }

  @GetMapping("/student/paid-schooling")
  public List<StateEtudiantBranchDTO> getAllEtudiantScolaritePayee() {
    return stateService.getAllEtudiantScolaritePayee();
  }

  @GetMapping("/student/unpaid-schooling")
  public List<StateEtudiantBranchDTO> getAllEtudiantScolariteImpayee() {
    return stateService.getAllEtudiantScolariteImpayee();
  }

  @GetMapping("/student/registration")
  public List<StateEtudiantBranchDTO> getAllEtudiantInscription() {
    return stateService.getAllEtudiantInscription();
  }

  @GetMapping("/campus/by/branch")
  public List<CampusBranchDTO> getAllCampusByBranch() {
    return stateService.getAllCampusByBranch();
  }

  @GetMapping("/session/open/by/branch")
  public List<SessionBranchDTO> getAllSessionOuverte() {
    return stateService.getAllSessionOuverte();
  }

  @GetMapping("/current/session/by/branch")
  public List<SessionBranchDTO> getAllSessionSurAnnee() {
    return stateService.getAllSessionSurAnnee();
  }
}
