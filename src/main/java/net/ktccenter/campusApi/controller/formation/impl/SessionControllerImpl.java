package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.SessionController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SessionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.scolarite.SessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/formation/sessions")
@RestController
@CrossOrigin("*")
public class SessionControllerImpl implements SessionController {
  private final SessionService service;

  public SessionControllerImpl(SessionService service) {
    this.service = service;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SessionDTO save(@Valid @RequestBody SessionRequestDTO dto) {
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  public List<LiteSessionDTO> saveAll(@Valid @RequestBody List<ImportSessionRequestDTO> dtos) {
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  public SessionDTO findById(@PathVariable("id") Long id) {
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("La session  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  public List<SessionBranchDTO> list() {
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteSessionDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  public void update(@Valid @RequestBody SessionRequestDTO dto, @PathVariable("id") Long id) {
    if (service.findById(id) == null) throw new APIException("La session avec l'id " + id + " n'existe pas");
    /*if (service.equalsByDto(dto, id))
      throw new APIException("La session avec les données suivante : " + dto.toString() + " existe déjà");*/
    service.update(dto, id);
  }

  @Override
  @PostMapping("/close/{id}")
  public SessionDTO close(@PathVariable("id") Long id) {
    return service.close(id);
  }
}