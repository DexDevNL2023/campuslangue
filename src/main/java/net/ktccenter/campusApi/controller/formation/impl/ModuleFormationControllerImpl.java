package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.ModuleFormationController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportModuleFormationRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModuleFormationDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModuleFormationRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.ModuleFormationService;
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

  public ModuleFormationControllerImpl(ModuleFormationService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ModuleFormationDTO save(@Valid @RequestBody ModuleFormationRequestDTO dto) {
    if (service.existByCode(dto.getCode()))
      throw new APIException("Le module formation avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteModuleFormationDTO> saveAll(@Valid @RequestBody List<ImportModuleFormationRequestDTO> dtos) {
    for (ImportModuleFormationRequestDTO dto : dtos) {
      if (service.existByCode(dto.getCode()))
        throw new APIException("Le module formation avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public ModuleFormationDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le module formation  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LiteModuleFormationDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteModuleFormationDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody ModuleFormationRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("Le module formation avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le module formation avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}