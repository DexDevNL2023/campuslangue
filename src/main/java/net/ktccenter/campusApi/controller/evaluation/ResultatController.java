package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.reponse.cours.ExamenForResultatReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForResultatReponseDTO;
import net.ktccenter.campusApi.enums.ResultatFilter;
import net.ktccenter.campusApi.enums.ResultatState;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/resultat")
@RestController
@CrossOrigin("*")
public class ResultatController {
  private final TestModuleService testModuleService;
  private final ExamenService examenService;

  public ResultatController(TestModuleService testModuleService, ExamenService examenService) {
      this.testModuleService = testModuleService;
      this.examenService = examenService;
  }

  @GetMapping("/test-module/{sessionId}/{moduleId}/{state}/{order}")
  @ResponseStatus(HttpStatus.OK)
  public List<TestModuleForResultatReponseDTO> getAllResultatTestBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("moduleId") Long moduleId,
                                                                     @PathVariable("state") ResultatState state, @PathVariable("order") ResultatFilter order) {
    return testModuleService.getAllResultatTestBySession(sessionId, moduleId, state, order);
  }

  @GetMapping("/examen/{sessionId}/{state}/{order}")
  @ResponseStatus(HttpStatus.OK)
  public List<ExamenForResultatReponseDTO> getAllResultatExamenBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("state") ResultatState state,
                                                                         @PathVariable("order") ResultatFilter order) {
    return examenService.getAllResultatExamenBySession(sessionId, state, order);
  }
}