package net.ktccenter.campusApi.controller.structure.impl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.ktccenter.campusApi.controller.structure.CampusController;
import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CampusBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.CampusService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/campus")
@RestController
@CrossOrigin("*")
public class CampusControllerImpl implements CampusController {
  private final CampusService service;
  private final AutorisationService autorisationService;

  @Autowired
  private MainService mainService;


    public CampusControllerImpl(CampusService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CampusDTO save(@Valid @RequestBody CampusRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Ajouter un campus", "campus-add", "POST", true));
      if (service.existsByCodeAndLibelle(dto.getCode(), dto.getLibelle()))
          throw new RuntimeException("Le campus avec le code " + dto.getCode() + ", ou le libelle " + dto.getLibelle() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "campus-import")
  public List<LiteCampusDTO> saveAll(@Valid @RequestBody List<ImportCampusRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Importer des campus", "campus-import", "POST", true));
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
  //@AuthorizeUser(actionKey = "campus-list")
  @SecurityRequirement(name = "Authorization")
  public List<CampusBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Lister les campus", "campus-list", "GET", true));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteCampusDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "campus-update")
  public void update(@Valid @RequestBody CampusRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("STRUCTURE", "Mttre à jour un campus", "campus-update", "PUT", true));
    if (service.findById(id) == null) throw new RuntimeException("Le campus avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new RuntimeException("Le campus avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}
