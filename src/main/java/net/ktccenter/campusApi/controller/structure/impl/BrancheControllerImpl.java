package net.ktccenter.campusApi.controller.structure.impl;

import net.ktccenter.campusApi.controller.structure.BrancheController;
import net.ktccenter.campusApi.dto.importation.administration.ImportBrancheRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.reponse.administration.BrancheDTO;
import net.ktccenter.campusApi.dto.request.administration.BrancheRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.BrancheService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/structure/branches")
@RestController
@CrossOrigin("*")
public class BrancheControllerImpl implements BrancheController {
  private final BrancheService service;
    private final AutorisationService autorisationService;

    public BrancheControllerImpl(BrancheService service, AutorisationService autorisationService) {
    this.service = service;
        this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "branche-add")
  public BrancheDTO save(@Valid @RequestBody BrancheRequestDTO dto) {
      autorisationService.addDroit(new SaveDroitDTO("Structure", "Ajouter un branche", "branche-add", "POST", true));
      if (service.existsByCodeAndVille(dto.getCode(), dto.getVille()))
          throw new RuntimeException("La branche avec le code " + dto.getCode() + ", ou la ville " + dto.getVille() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "branche-import")
  public List<LiteBrancheDTO> saveAll(@Valid @RequestBody List<ImportBrancheRequestDTO> dtos) {
      autorisationService.addDroit(new SaveDroitDTO("Structure", "Importer des branche", "branche-import", "POST", true));
    for (ImportBrancheRequestDTO dto : dtos) {
        if (service.existsByCodeAndVille(dto.getCode(), dto.getVille()))
            throw new RuntimeException("La branche avec le code " + dto.getCode() + ", ou la ville " + dto.getVille() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "branche-find")
  public BrancheDTO findById(@PathVariable("id") Long id) {
      autorisationService.addDroit(new SaveDroitDTO("Structure", "Afficher les détails d'un branche", "branche-find", "GET", true));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "branche-delet")
  public void delete(@PathVariable("id") Long id) {
      autorisationService.addDroit(new SaveDroitDTO("Structure", "Supprimer un branche", "branche-delet", "DELET", true));
      if (service.findById(id) == null) throw new RuntimeException("La branche avec l'id " + id + " n'existe pas");
      service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "branche-list")
  public List<LiteBrancheDTO> list() {
      autorisationService.addDroit(new SaveDroitDTO("Structure", "Lister les branches", "branche-list", "GET", true));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteBrancheDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "branche-update")
  public void update(@Valid @RequestBody BrancheRequestDTO dto, @PathVariable("id") Long id) {
      autorisationService.addDroit(new SaveDroitDTO("Structure", "Mttre à jour un branche", "branche-update", "PUT", true));
      if (service.findById(id) == null) throw new RuntimeException("La branche avec l'id " + id + " n'existe pas");
      if (service.equalsByDto(dto, id))
          throw new RuntimeException("La branche avec les données suivante : " + dto.toString() + " existe déjà");
      service.update(dto, id);
  }
}
