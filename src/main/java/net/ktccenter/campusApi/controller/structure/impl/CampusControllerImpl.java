package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.CampusController;
import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.service.administration.CampusService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/campus")
@RestController
@CrossOrigin("*")
public class CampusControllerImpl implements CampusController {
  private final CampusService service;

  public CampusControllerImpl(CampusService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CampusDTO save(@Valid @RequestBody CampusRequestDTO dto) {
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
          throw new RuntimeException("Le campus avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteCampusDTO> saveAll(@Valid @RequestBody List<ImportCampusRequestDTO> dtos) {
    for (ImportCampusRequestDTO dto : dtos) {
        if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
            throw new RuntimeException("Le campus avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public CampusDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "")
  public List<LiteCampusDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteCampusDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public CampusDTO update(@Valid @RequestBody CampusRequestDTO dto, @PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("Le campus avec les données suivante : " + dto.toString() + " existe déjà");
      return service.update(dto, id);
  }
}
