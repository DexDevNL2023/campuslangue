package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.BrancheController;
import net.ktccenter.campusApi.dto.importation.administration.ImportBrancheRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.reponse.administration.BrancheDTO;
import net.ktccenter.campusApi.dto.request.administration.BrancheRequestDTO;
import net.ktccenter.campusApi.service.administration.BrancheService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/branches")
@RestController
@CrossOrigin("*")
public class BrancheControllerImpl implements BrancheController {
  private final BrancheService service;

  public BrancheControllerImpl(BrancheService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BrancheDTO save(@Valid @RequestBody BrancheRequestDTO dto) {
      if (service.existsByCodeAndVille(dto.getCode(), dto.getVille()))
          throw new RuntimeException("La branche avec le code " + dto.getCode() + ", ou la ville " + dto.getVille() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteBrancheDTO> saveAll(@Valid @RequestBody List<ImportBrancheRequestDTO> dtos) {
    for (ImportBrancheRequestDTO dto : dtos) {
        if (service.existsByCodeAndVille(dto.getCode(), dto.getVille()))
            throw new RuntimeException("La branche avec le code " + dto.getCode() + ", ou la ville " + dto.getVille() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public BrancheDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("La branche avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LiteBrancheDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteBrancheDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public BrancheDTO update(@Valid @RequestBody BrancheRequestDTO dto, @PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("La branche avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("La branche avec les données suivante : " + dto.toString() + " existe déjà");
      return service.update(dto, id);
  }
}
