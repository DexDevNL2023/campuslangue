package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.PaiementController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementForCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchAndCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.PaiementDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.PaiementRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.PaiementService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/paiements")
@RestController
@CrossOrigin("*")
public class PaiementControllerImpl implements PaiementController {
  private final PaiementService service;
  private final AutorisationService autorisationService;

  public PaiementControllerImpl(PaiementService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "versement-add")
  public PaiementDTO save(@Valid @RequestBody PaiementRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Ajouter un versement", "versement-add", "POST", false));
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "versement-import")
  public List<LitePaiementDTO> saveAll(@Valid @RequestBody List<ImportPaiementRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Importer des versements", "versement-import", "POST", false));
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "versement-details")
  public PaiementDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Détails d'un versement", "versement-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "versement-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Supprimer un versement", "versement-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("Le paiement  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "versement-list")
  public List<PaiementBranchDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Lister les versements", "versement-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/branch/and/campus")
  @AuthorizeUser(actionKey = "versement-list")
  public List<PaiementBranchAndCampusDTO> listAllAndGroupByBranchAndCampus() {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Lister les versements", "versement-list", "GET", false));
    return service.findAllAndGroupByBranchAndCampus();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LitePaiementDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "versement-edit")
  public void update(@Valid @RequestBody PaiementRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Modifier un versement", "versement-edit", "PUT", false));
    if (service.findById(id) == null) throw new APIException("Le paiement avec l'id " + id + " n'existe pas");
    service.update(dto, id);
  }

  @Override
  @GetMapping("/campus/{campusId}")
  @AuthorizeUser(actionKey = "versement-list")
  public List<LitePaiementForCampusDTO> listByCampus(@PathVariable("campusId") Long campusId) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Lister les versements", "versement-list", "GET", false));
    return service.findAllByCampus(campusId);
  }
}