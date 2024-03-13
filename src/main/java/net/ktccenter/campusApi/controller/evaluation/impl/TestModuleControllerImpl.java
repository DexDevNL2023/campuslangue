package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.TestModuleController;
import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.branch.TestModuleBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleRequestDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/tests-module")
@RestController
@CrossOrigin("*")
public class TestModuleControllerImpl implements TestModuleController {
  private final TestModuleService service;
  private final AutorisationService autorisationService;

  public TestModuleControllerImpl(TestModuleService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "test-module-add")
  public TestModuleDTO save(@Valid @RequestBody TestModuleRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Ajouter un test-module", "test-module-add", "POST", false));
    if (service.existsByCode(dto.getCode()))
      throw new RuntimeException("Le test module avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "test-module-import")
  public List<LiteTestModuleDTO> saveAll(@Valid @RequestBody List<ImportTestModuleRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des test-modules", "test-module-import", "POST", false));
    for (ImportTestModuleRequestDTO dto : dtos) {
      if (service.existsByCode(dto.getCode()))
        throw new RuntimeException("Le test module avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "test-module-details")
  public TestModuleDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Détails d'un test-module", "test-module-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "test-module-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Supprimer un test-module", "test-module-delet", "DELET", false));
    if (service.findById(id) == null) throw new RuntimeException("Le test module avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "test-module-list")
  public List<TestModuleBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Lister les test-modules", "test-module-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteTestModuleDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "test-module-edit")
  public void update(@Valid @RequestBody TestModuleRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Modifier un test-module", "test-module-edit", "PUT", false));
    if (service.findById(id) == null) throw new RuntimeException("Le test module avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("Le test module avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}