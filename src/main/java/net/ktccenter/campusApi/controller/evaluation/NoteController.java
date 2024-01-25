package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleForNoteDTO;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
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

  public NoteController(TestModuleService testModuleService, ExamenService examenService) {
    this.testModuleService = testModuleService;
    this.examenService = examenService;
  }

  @GetMapping("/get/test-module/{sessionId}/{moduleId}")
  @ResponseStatus(HttpStatus.OK)
  public List<TestModuleForNoteReponseDTO> getAllTestBySession(@PathVariable("sessionId") Long sessionId,  @PathVariable("moduleId") Long moduleId) {
    return testModuleService.getAllTestBySession(sessionId, moduleId);
  }

  @GetMapping("/get/examen/{sessionId}/{uniteId}")
  @ResponseStatus(HttpStatus.OK)
  public List<ExamenForNoteReponseDTO> getAllExamenBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("uniteId") Long uniteId) {
    return examenService.getAllExamenBySession(sessionId, uniteId);
  }

  @PostMapping("/saisie/notes/test-module")
  @ResponseStatus(HttpStatus.OK)
  public List<TestModuleForNoteReponseDTO> saisieNotesTest(@Valid @RequestBody List<TestModuleForNoteDTO> dtos) {
    return testModuleService.saisieNotesTest(dtos);
  }

  @PostMapping("/saisie/notes/examen")
  @ResponseStatus(HttpStatus.OK)
  public List<ExamenForNoteReponseDTO> saisieNotesexamen(@Valid @RequestBody List<ExamenForNoteDTO> dtos) {
    return examenService.saisieNotesexamen(dtos);
  }
}
