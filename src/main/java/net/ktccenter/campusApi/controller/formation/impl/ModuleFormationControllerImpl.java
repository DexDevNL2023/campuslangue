package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.ModuleFormationController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportModuleFormationRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModuleFormationDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModuleFormationRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.ModuleFormationService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/formation/modules-formation")
@RestController
@CrossOrigin("*")
public class ModuleFormationControllerImpl implements ModuleFormationController {
  private final ModuleFormationService service;
  private final AutorisationService autorisationService;

  public ModuleFormationControllerImpl(ModuleFormationService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "module-formation-add")
  public ModuleFormationDTO save(@Valid @RequestBody ModuleFormationRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Ajouter un module-formation", "module-formation-add", "POST", false));
    if (service.existByCode(dto.getCode()))
      throw new APIException("Le module formation avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "module-formation-import")
  public List<LiteModuleFormationDTO> saveAll(@Valid @RequestBody List<ImportModuleFormationRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Importer des module-formations", "module-formation-import", "POST", false));
    for (ImportModuleFormationRequestDTO dto : dtos) {
      if (service.existByCode(dto.getCode()))
        throw new APIException("Le module formation avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "module-formation-details")
  public ModuleFormationDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Détails d'un module-formation", "module-formation-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "module-formation-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Supprimer un module-formation", "module-formation-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("Le module formation  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "module-formation-list")
  public List<LiteModuleFormationDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Lister les module-formations", "module-formation-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteModuleFormationDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "module-formation-edit")
  public void update(@Valid @RequestBody ModuleFormationRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Modifier un module-formation", "module-formation-edit", "PUT", false));
    if (service.findById(id) == null) throw new APIException("Le module formation avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le module formation avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}