package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.UniteController;
import net.ktccenter.campusApi.dto.importation.cours.ImportUniteRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.reponse.cours.UniteDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.UniteRequestDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.UniteService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/unites-formation")
@RestController
@CrossOrigin("*")
public class UniteControllerImpl implements UniteController {
  private final UniteService service;
  private final AutorisationService autorisationService;

  public UniteControllerImpl(UniteService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "unite-add")
  public UniteDTO save(@Valid @RequestBody UniteRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Ajouter une unite", "unite-add", "POST", false));
    if (service.existsByCode(dto.getCode()))
      throw new RuntimeException("L'unite avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "unite-import")
  public List<LiteUniteDTO> saveAll(@Valid @RequestBody List<ImportUniteRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des unites", "unite-import", "POST", false));
    for (ImportUniteRequestDTO dto : dtos) {
      if (service.existsByCode(dto.getCode()))
        throw new RuntimeException("L'unite avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "unite-details")
  public UniteDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Détails d'une unite", "unite-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "unite-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Supprimer une unite", "unite-delet", "DELET", false));
    if (service.findById(id) == null) throw new RuntimeException("L'unite avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "unite-list")
  public List<LiteUniteDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Lister les unites", "unite-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteUniteDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "unite-edit")
  public void update(@Valid @RequestBody UniteRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Modifier une unite", "unite-edit", "PUT", false));
    if (service.findById(id) == null) throw new RuntimeException("L'unite avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'unite avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}