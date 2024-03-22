package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.*;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import net.ktccenter.campusApi.service.scolarite.EtudiantService;
import net.ktccenter.campusApi.service.scolarite.SessionService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/evaluation/notes")
@RestController
@CrossOrigin("*")
public class NoteController {
  private final TestModuleService testModuleService;
  private final ExamenService examenService;
  private final SessionService sessionService;
  private final EtudiantService etudiantService;
  private final MainService mainService;
  private final AutorisationService autorisationService;

  public NoteController(TestModuleService testModuleService, ExamenService examenService, SessionService sessionService, EtudiantService etudiantService, MainService mainService, AutorisationService autorisationService) {
    this.testModuleService = testModuleService;
    this.examenService = examenService;
    this.sessionService = sessionService;
      this.etudiantService = etudiantService;
      this.mainService = mainService;
    this.autorisationService = autorisationService;
  }

  @GetMapping("/get/all/session")
  @ResponseStatus(HttpStatus.OK)
  public List<LiteSessionForNoteDTO> getAllSession() {
    return sessionService.getAllSessionByCurrentUser();
  }

  @GetMapping("/get/test-module/{sessionId}/{moduleId}")
  @ResponseStatus(HttpStatus.OK)
  public FullTestModuleForNoteDTO getAllTestBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("moduleId") Long moduleId) {
    return testModuleService.getAllTestBySession(sessionId, moduleId);
  }

  @PostMapping("/saisie/notes/test-module")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "saisie-notes-test-module")
  public List<TestModuleForNoteReponseDTO> saisieNotesTest(@Valid @RequestBody FullTestModuleForNoteDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Saisie des notes de test module", "saisie-notes-test-module", "POST", false));
    return testModuleService.saisieNotesTest(dto);
  }

  @GetMapping("/get/multiple/examen/{sessionId}")
  @ResponseStatus(HttpStatus.OK)
  public FullExamen2ForNoteDTO getAllExamenBySession(@PathVariable("sessionId") Long sessionId) {
    return examenService.getAllExamenBySession(sessionId);
  }

  @PostMapping("/saisie/multiple/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "saisie-multiple-notes-examen")
  public List<ExamenForNoteReponseDTO> saisieNotesExamen2(@Valid @RequestBody FullExamen2ForNoteDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Saisie multiple des notes d'examen", "saisie-multiple-notes-examen", "POST", false));
    return examenService.saisieNotesExamen2(dto);
  }

  @GetMapping("/get/examen/{sessionId}/{uniteId}")
  @ResponseStatus(HttpStatus.OK)
  public FullExamenForNoteDTO getAllExamenBySessionAndUnite(@PathVariable("sessionId") Long sessionId, @PathVariable("uniteId") Long uniteId) {
    return examenService.getAllExamenBySessionAndUnite(sessionId, uniteId);
  }

  @PostMapping("/saisie/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "saisie-notes-examen")
  public List<ExamenForNoteReponseDTO> saisieNotesExamen(@Valid @RequestBody FullExamenForNoteDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Saisie des notes d'examen", "saisie-notes-examen", "POST", false));
    return examenService.saisieNotesExamen(dto);
  }

  @PostMapping("/import/notes/test-module")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "import-notes-test-module")
  public void importNotesTest(@Valid @RequestBody FullTestModuleForNoteImportDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des notes de test module", "import-notes-test-module", "POST", false));
    testModuleService.importNotesTest(dto);
  }

  @PostMapping("/import/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "import-notes-examen")
  public void importNotesexamen(@Valid @RequestBody FullExamenForNoteImportDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des notes d'examen'", "import-notes-examen", "POST", false));
    examenService.importNotesExamen(dto);
  }

  @GetMapping("/is/rattrapage")
  public List<EtudiantBranchDTO> getAllIsRattapage() {
    List<EtudiantBranchDTO> result = etudiantService.getAllIsRattapage();
    if (result.isEmpty()) return getEmptyList();
    return result;
  }

  private List<EtudiantBranchDTO> getEmptyList() {
    List<EtudiantBranchDTO> result = new ArrayList<>();
    EtudiantBranchDTO dto = new EtudiantBranchDTO();
    dto.setBranche(new LiteBrancheDTO(mainService.getCurrentUserBranch()));
    dto.setData(new ArrayList<>());
    result.add(dto);
    return result;
  }

  @GetMapping("/is/rattrapage/{sessionId}/{uniteId}")
  public FullExamenForNoteDTO getAllNoteStudentIsRattapage(@PathVariable("sessionId") Long sessionId, @PathVariable("uniteId") Long uniteId) {
    return examenService.getAllNoteStudentIsRattapage(sessionId, uniteId);
  }
}
