package net.ktccenter.campusApi.controller.formation.impl;

import net.ktccenter.campusApi.controller.formation.FormateurController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.branch.FormateurBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.FormateurService;
import net.ktccenter.campusApi.utils.MyUtils;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/scolarite/formateurs")
@RestController
@CrossOrigin("*")
public class FormateurControllerImpl implements FormateurController {
  private final FormateurService service;
    private final MainService mainService;
  private final AutorisationService autorisationService;

  public FormateurControllerImpl(FormateurService service, MainService mainService, AutorisationService autorisationService) {
    this.service = service;
        this.mainService = mainService;
    this.autorisationService = autorisationService;
  }
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "formateur-add")
  public FormateurDTO save(@Valid @RequestBody FormateurRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Ajouter un formateur", "formateur-add", "POST", false));
    if(!MyUtils.isValidEmailAddress(dto.getEmail()))
      throw new APIException("L'email " + dto.getEmail() + " est invalide.");
    if (service.existByEmail(dto.getEmail()))
      throw new APIException("Le formateur avec l'email " + dto.getEmail() + " existe déjà");
    if (service.existByTelephone(dto.getEmail()))
      throw new APIException("Le formateur avec le téléphone " + dto.getTelephone() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "formateur-import")
  public List<LiteFormateurDTO> saveAll(@Valid @RequestBody List<ImportFormateurRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Importer des formateurs", "formateur-import", "POST", false));
    for (ImportFormateurRequestDTO dto : dtos) {
      if(!MyUtils.isValidEmailAddress(dto.getEmail()))
        throw new APIException("L'email " + dto.getEmail() + " est invalide.");
      if (service.existByEmail(dto.getEmail()))
        throw new APIException("Le formateur avec l'email " + dto.getEmail() + " existe déjà");
      if (service.existByTelephone(dto.getEmail()))
        throw new APIException("Le formateur avec le téléphone " + dto.getTelephone() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "formateur-details")
  public FormateurDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Détails d'un formateur", "formateur-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "formateur-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Supprimer un formateur", "formateur-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("Le formateur avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "formateur-list")
  public List<FormateurBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Lister les formateurs", "formateur-list", "GET", false));
      List<FormateurBranchDTO> result = service.findAll();
      if (result.isEmpty()) return getEmptyList();
      return result;
  }

  @Override
  @GetMapping("/by/branch/{branchId}")
  @AuthorizeUser(actionKey = "formateur-list")
  public List<LiteFormateurDTO> listByBranch(@PathVariable("branchId") Long branchId) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Lister les formateurs", "formateur-list", "GET", false));
    return service.findAllByBranch(branchId);
  }

  private List<FormateurBranchDTO> getEmptyList() {
      List<FormateurBranchDTO> result = new ArrayList<>();
      FormateurBranchDTO dto = new FormateurBranchDTO();
      dto.setBranche(new LiteBrancheDTO(mainService.getCurrentUserBranch()));
      dto.setData(new ArrayList<>());
      result.add(dto);
      return result;
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteFormateurDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "formateur-edit")
  public void update(@Valid @RequestBody FormateurRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Formations", "Modifier un formateur", "formateur-edit", "PUT", false));
    if(!MyUtils.isValidEmailAddress(dto.getEmail()))
      throw new APIException("L'email " + dto.getEmail() + " est invalide.");
    if (service.findById(id) == null) throw new APIException("Le formateur avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le formateur avec les données suivante : " + dto + " existe déjà");
    service.update(dto, id);
  }
}