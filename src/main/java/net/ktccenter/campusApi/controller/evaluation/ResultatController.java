package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.reponse.cours.ExamenForResultatReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForResultatReponseDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.enums.ResultatState;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/resultat")
@RestController
@CrossOrigin("*")
public class ResultatController {
  private final TestModuleService testModuleService;
  private final ExamenService examenService;
  private final AutorisationService autorisationService;

  public ResultatController(TestModuleService testModuleService, ExamenService examenService, AutorisationService autorisationService) {
      this.testModuleService = testModuleService;
      this.examenService = examenService;
    this.autorisationService = autorisationService;
  }

  @GetMapping("/test-module/{sessionId}/{moduleId}/{state}")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "resultat-test-module")
  public List<TestModuleForResultatReponseDTO> getAllResultatTestBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("moduleId") Long moduleId, @PathVariable("state") ResultatState state) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Afficher les resultats d'un test module", "resultat-test-module", "GET", false));
    return testModuleService.getAllResultatTestBySession(sessionId, moduleId, state);
  }

  @GetMapping("/examen/{sessionId}/{state}")
  @ResponseStatus(HttpStatus.OK)
  @AuthorizeUser(actionKey = "resultat-examen")
  public List<ExamenForResultatReponseDTO> getAllResultatExamenBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("state") ResultatState state) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Afficher les resultats d'un examen", "resultat-examen", "GET", false));
    return examenService.getAllResultatExamenBySession(sessionId, state);
  }
}
