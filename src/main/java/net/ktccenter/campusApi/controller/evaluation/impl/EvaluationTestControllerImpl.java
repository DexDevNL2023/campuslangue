package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.EvaluationTestController;
import net.ktccenter.campusApi.dto.importation.cours.ImportEvaluationTestRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EvaluationTestDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestRequestDTO;
import net.ktccenter.campusApi.service.cours.EvaluationTestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/evaluations-test")
@RestController
@CrossOrigin("*")
public class EvaluationTestControllerImpl implements EvaluationTestController {
  private final EvaluationTestService service;

  public EvaluationTestControllerImpl(EvaluationTestService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EvaluationTestDTO save(@Valid @RequestBody EvaluationTestRequestDTO dto) {
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteEvaluationTestDTO> saveAll(@Valid @RequestBody List<ImportEvaluationTestRequestDTO> dtos) {
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public EvaluationTestDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("L'evaluation test avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LiteEvaluationTestDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteEvaluationTestDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody EvaluationTestRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("L'evaluation test avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'evaluation test avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}