package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.ExamenController;
import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.reponse.branch.ExamenBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import net.ktccenter.campusApi.service.cours.ExamenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/examens")
@RestController
@CrossOrigin("*")
public class ExamenControllerImpl implements ExamenController {
  private final ExamenService service;

  public ExamenControllerImpl(ExamenService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ExamenDTO save(@Valid @RequestBody ExamenRequestDTO dto) {
    if (service.existsByCode(dto.getCode()))
      throw new RuntimeException("L'examen avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteExamenDTO> saveAll(@Valid @RequestBody List<ImportExamenRequestDTO> dtos) {
    for (ImportExamenRequestDTO dto : dtos) {
      if (service.existsByCode(dto.getCode()))
        throw new RuntimeException("L'examen avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public ExamenDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("L'examen avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<ExamenBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteExamenDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody ExamenRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("L'examen avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'examen avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}