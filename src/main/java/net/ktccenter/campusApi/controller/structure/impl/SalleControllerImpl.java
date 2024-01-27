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
import net.ktccenter.campusApi.service.administration.SalleService;
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
  private final CampusRepository campusRepository;

  public SalleControllerImpl(SalleService service, CampusRepository campusRepository) {
    this.service = service;
    this.campusRepository = campusRepository;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SalleDTO save(@Valid @RequestBody SalleRequestDTO dto) {
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
          throw new RuntimeException("La salle avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteSalleDTO> saveAll(@Valid @RequestBody List<ImportSalleRequestDTO> dtos) {
    for (ImportSalleRequestDTO dto : dtos) {
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
        throw new RuntimeException("La salle avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public SalleDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("La salle avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<SalleBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteSalleDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody SalleRequestDTO dto, @PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("La salle avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("La salle avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }

  @Override
  @PostMapping("/change/occupation")
  public OccupationSalleDTO changeOccupation(@Valid @RequestBody OccupationSalleRequestDTO dto) {
    return service.changeOccupation(dto);
  }

  @Override
  @GetMapping("/occupation/by/id/{occupationId}")
  public OccupationSalleDTO getOccupationById(@Valid @PathVariable("occupationId") Long occupationId) {
    return service.getOccupationById(occupationId);
  }

  @Override
  @GetMapping("/occupation/by/code/{occupationCode}")
  public OccupationSalleDTO getOccupationByCode(@Valid @PathVariable("occupationCode") String occupationCode) {
    return service.getOccupationByCode(occupationCode);
  }

  @Override
  @GetMapping("/get/all/disponibilites/{salleId}")
  public List<LiteOccupationSalleDTO> getPlageDisponible(@Valid @PathVariable("salleId") Long salleId) {
    return service.getPlageDisponible(salleId);
  }

  @Override
  @GetMapping("/get/planning/{salleId}")
  public List<LiteOccupationSalleDTO> getPlannigSalle(@Valid @PathVariable("salleId") Long salleId) {
    return service.getPlannigSalle(salleId);
  }
}
