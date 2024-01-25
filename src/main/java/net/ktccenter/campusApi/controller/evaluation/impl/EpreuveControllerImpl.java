package net.ktccenter.campusApi.controller.evaluation.impl;

import net.ktccenter.campusApi.controller.evaluation.EpreuveController;
import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import net.ktccenter.campusApi.service.cours.EpreuveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/evaluation/epreuves")
@RestController
@CrossOrigin("*")
public class EpreuveControllerImpl implements EpreuveController {
  private final EpreuveService service;

  public EpreuveControllerImpl(EpreuveService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EpreuveDTO save(@Valid @RequestBody EpreuveRequestDTO dto) {
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteEpreuveDTO> saveAll(@Valid @RequestBody List<ImportEpreuveRequestDTO> dtos) {
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public EpreuveDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("L'epreuve avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LiteEpreuveDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteEpreuveDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public EpreuveDTO update(@Valid @RequestBody EpreuveRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new RuntimeException("L'epreuve avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("L'epreuve avec les données suivante : " + dto.toString() + " existe déjà");
    return service.update(dto, id);
  }
}