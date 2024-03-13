package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.ExamenController;
import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.reponse.branch.ExamenBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/examens")
@RestController
@CrossOrigin("*")
public class ExamenControllerImpl implements ExamenController {
  private final ExamenService service;
  private final AutorisationService autorisationService;

  public ExamenControllerImpl(ExamenService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "examen-add")
  public ExamenDTO save(@Valid @RequestBody ExamenRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Ajouter un examen", "examen-add", "POST", false));
    if (service.existsByCode(dto.getCode()))
      throw new RuntimeException("L'examen avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "examen-import")
  public List<LiteExamenDTO> saveAll(@Valid @RequestBody List<ImportExamenRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des examens", "examen-import", "POST", false));
    for (ImportExamenRequestDTO dto : dtos) {
      if (service.existsByCode(dto.getCode()))
        throw new RuntimeException("L'examen avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "examen-details")
  public ExamenDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Détails d'un examen", "examen-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "examen-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Supprimer un examen", "examen-delet", "DELET", false));
    if (service.findById(id) == null) throw new RuntimeException("L'examen avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "examen-list")
  public List<ExamenBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Lister les examens", "examen-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteExamenDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "examen-edit")
  public void update(@Valid @RequestBody ExamenRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Modifier un examen", "examen-edit", "PUT", false));
    if (service.findById(id) == null) throw new RuntimeException("L'examen avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'examen avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}