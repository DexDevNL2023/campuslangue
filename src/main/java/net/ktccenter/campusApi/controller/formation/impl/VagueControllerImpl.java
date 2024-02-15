package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.VagueController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.branch.VagueBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.VagueService;
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

  public VagueControllerImpl(VagueService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public VagueDTO save(@Valid @RequestBody VagueRequestDTO dto) {
    if (service.existByCode(dto.getCode()))
      throw new APIException("La vague avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteVagueDTO> saveAll(@Valid @RequestBody List<ImportVagueRequestDTO> dtos) {
    for (ImportVagueRequestDTO dto : dtos) {
      if (service.existByCode(dto.getCode()))
        throw new APIException("La vague avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public VagueDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("La vague  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<VagueBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/by/branch/{branchId}")
  //@AuthorizeUser(actionKey = "salle-list")
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
  public void update(@Valid @RequestBody VagueRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("La vague avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("La vague avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}