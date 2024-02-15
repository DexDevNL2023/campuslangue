package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.*;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import net.ktccenter.campusApi.service.scolarite.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/notes")
@RestController
@CrossOrigin("*")
public class NoteController {
  private final TestModuleService testModuleService;
  private final ExamenService examenService;
  private final SessionService sessionService;

  public NoteController(TestModuleService testModuleService, ExamenService examenService, SessionService sessionService) {
    this.testModuleService = testModuleService;
    this.examenService = examenService;
    this.sessionService = sessionService;
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
  public List<TestModuleForNoteReponseDTO> saisieNotesTest(@Valid @RequestBody FullTestModuleForNoteDTO dto) {
    return testModuleService.saisieNotesTest(dto);
  }

  @GetMapping("/get/multiple/examen/{sessionId}")
  @ResponseStatus(HttpStatus.OK)
  public FullExamen2ForNoteDTO getAllExamenBySession(@PathVariable("sessionId") Long sessionId) {
    return examenService.getAllExamenBySession(sessionId);
  }

  @PostMapping("/saisie/multiple/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  public List<ExamenForNoteReponseDTO> saisieNotesExamen2(@Valid @RequestBody FullExamen2ForNoteDTO dto) {
    return examenService.saisieNotesExamen2(dto);
  }

  @GetMapping("/get/examen/{sessionId}/{uniteId}")
  @ResponseStatus(HttpStatus.OK)
  public FullExamenForNoteDTO getAllExamenBySessionAndUnite(@PathVariable("sessionId") Long sessionId, @PathVariable("uniteId") Long uniteId) {
    return examenService.getAllExamenBySessionAndUnite(sessionId, uniteId);
  }

  @PostMapping("/saisie/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  public List<ExamenForNoteReponseDTO> saisieNotesExamen(@Valid @RequestBody FullExamenForNoteDTO dto) {
    return examenService.saisieNotesExamen(dto);
  }

  @PostMapping("/import/notes/test-module")
  @ResponseStatus(HttpStatus.OK)
  public void importNotesTest(@Valid @RequestBody FullTestModuleForNoteImportDTO dto) {
    testModuleService.importNotesTest(dto);
  }

  @PostMapping("/import/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  public void importNotesexamen(@Valid @RequestBody FullExamenForNoteImportDTO dto) {
    examenService.importNotesExamen(dto);
  }
}
