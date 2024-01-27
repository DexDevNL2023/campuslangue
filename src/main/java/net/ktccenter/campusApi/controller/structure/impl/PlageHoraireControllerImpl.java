package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.PlageHoraireController;
import net.ktccenter.campusApi.dto.importation.cours.ImportPlageHoraireRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageHoraireDTO;
import net.ktccenter.campusApi.dto.request.cours.PlageHoraireRequestDTO;
import net.ktccenter.campusApi.service.cours.PlageHoraireService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/plages_horaire")
@RestController
@CrossOrigin("*")
public class PlageHoraireControllerImpl implements PlageHoraireController {
  private final PlageHoraireService service;

  public PlageHoraireControllerImpl(PlageHoraireService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PlageHoraireDTO save(@Valid @RequestBody PlageHoraireRequestDTO dto) {
      if (service.existsByCode(dto.getCode()))
          throw new RuntimeException("La ressource avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LitePlageHoraireDTO> saveAll(@Valid @RequestBody List<ImportPlageHoraireRequestDTO> dtos) {
    for (ImportPlageHoraireRequestDTO dto : dtos) {
        if (service.existsByCode(dto.getCode()))
            throw new RuntimeException("La ressource avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public PlageHoraireDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("La ressource avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<LitePlageHoraireDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LitePlageHoraireDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody PlageHoraireRequestDTO dto, @PathVariable("id") Long id) {
      if (service.findById(id) == null) throw new RuntimeException("La ressource avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("La ressource avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}
