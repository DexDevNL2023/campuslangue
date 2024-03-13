package net.ktccenter.campusApi.controller.administration.impl;

import net.ktccenter.campusApi.controller.administration.JourOuvrableController;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.JourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RequestJourOuvrableDTO;
import net.ktccenter.campusApi.dto.request.administration.JourOuvrableRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.JourOuvrableService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
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
  @AuthorizeUser(actionKey = "jour-ouvrable-add")
  public JourOuvrableDTO save(@Valid @RequestBody JourOuvrableRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Administration", "Ajouter un jour ouvrable", "jour-ouvrable-add", "POST", true));
    return service.save(dto);
  }

  @Override
  @PostMapping("/save")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "jour-ouvrable-import")
  public List<JourOuvrableDTO> saveAll(@Valid @RequestBody List<JourOuvrableRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Administration", "Ajouter un jour ouvrable", "jour-ouvrable-add", "POST", true));
    return service.saveAll(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "jour-ouvrable-details")
  public JourOuvrableDTO findById(@PathVariable("id") Long id) {
      autorisationService.addDroit(new SaveDroitDTO("Administration", "Afficher les détails d'un jour ouvrable", "jour-ouvrable-details", "GET", true));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "jour-ouvrable-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Administration", "Supprimer un jour ouvrable", "jour-ouvrable-delet", "DELET", true));
    if (service.findById(id) == null) throw new RuntimeException("Le jour ouvrable avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "jour-ouvrable-list")
  public List<LiteJourOuvrableDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Administration", "Lister les jours ouvrables", "jour-ouvrable-list", "GET", true));
    return service.findAll();
  }

  @Override
  @GetMapping("/default")
  public List<RequestJourOuvrableDTO> getDefaultJour() {
    return service.getDefaultJour();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteJourOuvrableDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "jour-ouvrable-edit")
  public void update(@Valid @RequestBody JourOuvrableRequestDTO dto, @PathVariable("id") Long id) {
      autorisationService.addDroit(new SaveDroitDTO("Administration", "Mettre à jour un jour ouvrable", "jour-ouvrable-edit", "PUT", true));
    if (service.findById(id) == null) throw new RuntimeException("Le jour ouvrable avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("Le jour ouvrable avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}
