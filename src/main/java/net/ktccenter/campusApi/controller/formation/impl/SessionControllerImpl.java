package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.SessionController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SessionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.SessionService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
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
  private final AutorisationService autorisationService;

  public SessionControllerImpl(SessionService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "session-add")
  public SessionDTO save(@Valid @RequestBody SessionRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Ajouter une session", "session-add", "POST", false));
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "session-import")
  public List<LiteSessionDTO> saveAll(@Valid @RequestBody List<ImportSessionRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Importer des sessions", "session-import", "POST", false));
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "session-details")
  public SessionDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Détails d'une session", "session-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "session-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Supprimer une session", "session-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("La session  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "session-list")
  public List<SessionBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Lister les sessions", "session-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteSessionDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "session-edit")
  public void update(@Valid @RequestBody SessionRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Modifier une session", "session-edit", "PUT", false));
    if (service.findById(id) == null) throw new APIException("La session avec l'id " + id + " n'existe pas");
    service.update(dto, id);
  }

  @Override
  @PostMapping("/close/{id}")
  @AuthorizeUser(actionKey = "session-close")
  public SessionDTO close(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Clôturer une session", "session-close", "POST", false));
    return service.close(id);
  }
}