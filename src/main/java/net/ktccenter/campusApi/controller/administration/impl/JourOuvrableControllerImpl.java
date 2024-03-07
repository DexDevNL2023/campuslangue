package net.ktccenter.campusApi.controller.administration.impl;

import net.ktccenter.campusApi.controller.administration.JourOuvrableController;
import net.ktccenter.campusApi.dto.importation.administration.ImportJourOuvrableRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.JourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RequestJourOuvrableDTO;
import net.ktccenter.campusApi.dto.request.administration.JourOuvrableRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.JourOuvrableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/jour-ouvrable")
@RestController
@CrossOrigin("*")
public class JourOuvrableControllerImpl implements JourOuvrableController {
  private final JourOuvrableService service;
  private final AutorisationService autorisationService;


  public JourOuvrableControllerImpl(JourOuvrableService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  //@AuthorizeUser(actionKey = "campus-add")
  public JourOuvrableDTO save(@Valid @RequestBody JourOuvrableRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Administration", "Ajouter un jour ouvrable", "campus-add", "POST", true));
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  //@AuthorizeUser(actionKey = "campus-import")
  public List<LiteJourOuvrableDTO> saveAll(@Valid @RequestBody List<ImportJourOuvrableRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Importer des jours ouvrables", "campus-import", "POST", true));
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  //@AuthorizeUser(actionKey = "campus-find")
  public JourOuvrableDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Afficher les détails d'un campus", "campus-find", "GET", true));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  //@AuthorizeUser(actionKey = "campus-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Supprimer un campus", "campus-delet", "DELET", true));
    if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  //@AuthorizeUser(actionKey = "campus-list")
  public List<LiteJourOuvrableDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Lister les campus", "campus-list", "GET", true));
    return service.findAll();
  }

  @Override
  @GetMapping("/default")
  //@AuthorizeUser(actionKey = "campus-list")
  public List<RequestJourOuvrableDTO> getDefaultJour() {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Lister les campus", "campus-list", "GET", true));
    return service.getDefaultJour();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteJourOuvrableDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  //@AuthorizeUser(actionKey = "campus-update")
  public void update(@Valid @RequestBody JourOuvrableRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un campus", "campus-update", "PUT", true));
    if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("Le campus avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}
