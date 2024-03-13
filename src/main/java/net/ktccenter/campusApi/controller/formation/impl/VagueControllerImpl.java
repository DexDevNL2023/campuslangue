package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.VagueController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.branch.VagueBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.VagueService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/formation/vagues")
@RestController
@CrossOrigin("*")
public class VagueControllerImpl implements VagueController {
  private final VagueService service;
  private final AutorisationService autorisationService;

  public VagueControllerImpl(VagueService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "vague-add")
  public VagueDTO save(@Valid @RequestBody VagueRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Ajouter une vague", "vague-add", "POST", false));
    if (service.existByCode(dto.getCode()))
      throw new APIException("La vague avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "vague-import")
  public List<LiteVagueDTO> saveAll(@Valid @RequestBody List<ImportVagueRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Importer des vagues", "vague-import", "POST", false));
    for (ImportVagueRequestDTO dto : dtos) {
      if (service.existByCode(dto.getCode()))
        throw new APIException("La vague avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "vague-details")
  public VagueDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Détails d'une vague", "vague-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "vague-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Supprimer une vague", "vague-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("La vague  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "vague-list")
  public List<VagueBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Lister les vagues", "vague-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/by/branch/{branchId}")
  @AuthorizeUser(actionKey = "vague-list")
  public List<LiteVagueDTO> listByBranch(@PathVariable("branchId") Long branchId) {
    return service.findAllByBranch(branchId);
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteVagueDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "vague-edit")
  public void update(@Valid @RequestBody VagueRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Modifier une vague", "vague-edit", "PUT", false));
    if (service.findById(id) == null) throw new APIException("La vague avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("La vague avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}