package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.SalleController;
import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.OccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SalleBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.OccupationSalleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SalleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.SalleService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/salles")
@RestController
@CrossOrigin("*")
public class SalleControllerImpl implements SalleController {
  private final SalleService service;
  private final CampusRepository salleRepository;
  private final AutorisationService autorisationService;

  public SalleControllerImpl(SalleService service, CampusRepository salleRepository, AutorisationService autorisationService) {
    this.service = service;
    this.salleRepository = salleRepository;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "salle-add")
  public SalleDTO save(@Valid @RequestBody SalleRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Ajouter un salle", "salle-add", "POST", true));
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
          throw new RuntimeException("La salle avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "salle-import")
  public List<LiteSalleDTO> saveAll(@Valid @RequestBody List<ImportSalleRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Importer des salle", "salle-import", "POST", true));
    for (ImportSalleRequestDTO dto : dtos) {
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
        throw new RuntimeException("La salle avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "salle-find")
  public SalleDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Afficher les détails d'un salle", "salle-find", "GET", true));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "salle-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Supprimer un salle", "salle-delet", "DELET", true));
      if (service.findById(id) == null) throw new RuntimeException("La salle avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "salle-list")
  public List<SalleBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Lister les salle", "salle-list", "GET", true));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteSalleDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "salle-update")
  public void update(@Valid @RequestBody SalleRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un salle", "salle-update", "PUT", true));
      if (service.findById(id) == null) throw new RuntimeException("La salle avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("La salle avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }

  @Override
  @PostMapping("/change/occupation")
  public OccupationSalleDTO changeOccupation(@Valid @RequestBody OccupationSalleRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un salle", "salle-update", "PUT", true));
    return service.changeOccupation(dto);
  }

  @Override
  @GetMapping("/occupation/by/id/{occupationId}")
  public OccupationSalleDTO getOccupationById(@Valid @PathVariable("occupationId") Long occupationId) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un salle", "salle-update", "PUT", true));
    return service.getOccupationById(occupationId);
  }

  @Override
  @GetMapping("/occupation/by/code/{occupationCode}")
  public OccupationSalleDTO getOccupationByCode(@Valid @PathVariable("occupationCode") String occupationCode) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un salle", "salle-update", "PUT", true));
    return service.getOccupationByCode(occupationCode);
  }

  @Override
  @GetMapping("/get/all/disponibilites/{salleId}")
  public List<LiteOccupationSalleDTO> getPlageDisponible(@Valid @PathVariable("salleId") Long salleId) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un salle", "salle-update", "PUT", true));
    return service.getPlageDisponible(salleId);
  }

  @Override
  @GetMapping("/get/planning/{salleId}")
  public List<LiteOccupationSalleDTO> getPlannigSalle(@Valid @PathVariable("salleId") Long salleId) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un salle", "salle-update", "PUT", true));
    return service.getPlannigSalle(salleId);
  }
}
