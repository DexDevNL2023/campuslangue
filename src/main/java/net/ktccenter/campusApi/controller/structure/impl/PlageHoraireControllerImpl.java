package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.PlageHoraireController;
import net.ktccenter.campusApi.dto.importation.cours.ImportPlageHoraireRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageHoraireDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.cours.PlageHoraireRequestDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.cours.PlageHoraireService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
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
  private final AutorisationService autorisationService;

  public PlageHoraireControllerImpl(PlageHoraireService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "plage-add")
  public PlageHoraireDTO save(@Valid @RequestBody PlageHoraireRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Ajouter un plage", "plage-add", "POST", true));
      if (service.existsByCode(dto.getCode()))
          throw new RuntimeException("La ressource avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "plage-import")
  public List<LitePlageHoraireDTO> saveAll(@Valid @RequestBody List<ImportPlageHoraireRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Importer des plage", "plage-import", "POST", true));
    for (ImportPlageHoraireRequestDTO dto : dtos) {
        if (service.existsByCode(dto.getCode()))
            throw new RuntimeException("La ressource avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "plage-find")
  public PlageHoraireDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Afficher les détails d'un plage", "plage-find", "GET", true));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "plage-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Supprimer un plage", "plage-delet", "DELET", true));
      if (service.findById(id) == null) throw new RuntimeException("La ressource avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "plage-list")
  public List<LitePlageHoraireDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Lister les plage", "plage-list", "GET", true));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LitePlageHoraireDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "plage-update")
  public void update(@Valid @RequestBody PlageHoraireRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un plage", "plage-update", "PUT", true));
      if (service.findById(id) == null) throw new RuntimeException("La ressource avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("La ressource avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}
