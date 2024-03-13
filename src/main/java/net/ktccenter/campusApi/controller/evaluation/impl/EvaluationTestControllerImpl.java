package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.EvaluationTestController;
import net.ktccenter.campusApi.dto.importation.cours.ImportEvaluationTestRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EvaluationTestBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EvaluationTestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestRequestDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.EvaluationTestService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/evaluations-test")
@RestController
@CrossOrigin("*")
public class EvaluationTestControllerImpl implements EvaluationTestController {
  private final EvaluationTestService service;
  private final AutorisationService autorisationService;

  public EvaluationTestControllerImpl(EvaluationTestService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "evaluation-add")
  public EvaluationTestDTO save(@Valid @RequestBody EvaluationTestRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Ajouter une evaluation", "evaluation-add", "POST", false));
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "evaluation-import")
  public List<LiteEvaluationTestDTO> saveAll(@Valid @RequestBody List<ImportEvaluationTestRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des evaluations", "evaluation-import", "POST", false));
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "evaluation-details")
  public EvaluationTestDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Détails d'une evaluation", "evaluation-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "evaluation-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Supprimer une evaluation", "evaluation-delet", "DELET", false));
    if (service.findById(id) == null) throw new RuntimeException("L'evaluation test avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "evaluation-list")
  public List<EvaluationTestBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Lister les evaluations", "evaluation-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteEvaluationTestDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "evaluation-edit")
  public void update(@Valid @RequestBody EvaluationTestRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Modifier une evaluation", "evaluation-edit", "PUT", false));
    if (service.findById(id) == null) throw new RuntimeException("L'evaluation test avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'evaluation test avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}