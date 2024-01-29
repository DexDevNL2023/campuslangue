package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.TestModuleController;
import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.branch.TestModuleBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleRequestDTO;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/tests-module")
@RestController
@CrossOrigin("*")
public class TestModuleControllerImpl implements TestModuleController {
  private final TestModuleService service;

  public TestModuleControllerImpl(TestModuleService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TestModuleDTO save(@Valid @RequestBody TestModuleRequestDTO dto) {
    if (service.existsByCode(dto.getCode()))
      throw new RuntimeException("Le test module avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteTestModuleDTO> saveAll(@Valid @RequestBody List<ImportTestModuleRequestDTO> dtos) {
    for (ImportTestModuleRequestDTO dto : dtos) {
      if (service.existsByCode(dto.getCode()))
        throw new RuntimeException("Le test module avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public TestModuleDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("Le test module avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<TestModuleBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteTestModuleDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody TestModuleRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("Le test module avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("Le test module avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}