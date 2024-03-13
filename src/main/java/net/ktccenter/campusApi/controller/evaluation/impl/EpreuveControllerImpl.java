package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.EpreuveController;
import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EpreuveBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.EpreuveService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/epreuves")
@RestController
@CrossOrigin("*")
public class EpreuveControllerImpl implements EpreuveController {
  private final EpreuveService service;
  private final AutorisationService autorisationService;

  public EpreuveControllerImpl(EpreuveService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "epreuve-add")
  public EpreuveDTO save(@Valid @RequestBody EpreuveRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Ajouter une epreuve", "epreuve-add", "POST", false));
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "epreuve-import")
  public List<LiteEpreuveDTO> saveAll(@Valid @RequestBody List<ImportEpreuveRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Importer des epreuves", "epreuve-import", "POST", false));
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "epreuve-details")
  public EpreuveDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Détails d'une epreuve", "epreuve-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "epreuve-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Supprimer une epreuve", "epreuve-delet", "DELET", false));
    if (service.findById(id) == null) throw new RuntimeException("L'epreuve avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "epreuve-list")
  public List<EpreuveBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Lister les epreuves", "epreuve-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteEpreuveDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "epreuve-edit")
  public void update(@Valid @RequestBody EpreuveRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Notes", "Modifier une epreuve", "epreuve-edit", "PUT", false));
    if (service.findById(id) == null) throw new RuntimeException("L'epreuve avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'epreuve avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}