package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.CampusController;
import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.RessourceResponse;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.CampusService;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/campus")
@RestController
@CrossOrigin("*")
public class CampusControllerImpl implements CampusController {
  private final CampusService service;
  private final AutorisationService autorisationService;
  private final UserService userService;

  public CampusControllerImpl(CampusService service, AutorisationService autorisationService, UserService userService) {
    this.service = service;
    this.autorisationService = autorisationService;
    this.userService = userService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "campus-add")
  public CampusDTO save(@Valid @RequestBody CampusRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Ajouter un campus", "campus-add", "POST", true));
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
          throw new RuntimeException("Le campus avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "campus-add-list")
  public List<LiteCampusDTO> saveAll(@Valid @RequestBody List<ImportCampusRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Ajouter une liste de campus", "campus-add-list", "POST", true));
    for (ImportCampusRequestDTO dto : dtos) {
        if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
            throw new RuntimeException("Le campus avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "campus-find")
  public CampusDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Afficher les détails d'un campus", "campus-find", "GET", true));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "campus-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Supprimer un campus", "campus-delet", "DELET", true));
      if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "campus-list")
  public ResponseEntity<?> list() {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Lister les campus", "campus-list", "GET", true));
    return new ResponseEntity<>(new RessourceResponse(userService.getCurrentBranche(), service.findAll()), HttpStatus.OK);
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteCampusDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "campus-update")
  public CampusDTO update(@Valid @RequestBody CampusRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un campus", "campus-update", "PUT", true));
    if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("Le campus avec les données suivante : " + dto.toString() + " existe déjà");
    return service.update(dto, id);
  }
}
