package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.ModePaiementController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportModePaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModePaiementDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModePaiementRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.ModePaiementService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/modes-paiement")
@RestController
@CrossOrigin("*")
public class ModePaiementControllerImpl implements ModePaiementController {
  private final ModePaiementService service;
  private final AutorisationService autorisationService;

  public ModePaiementControllerImpl(ModePaiementService service, AutorisationService autorisationService) {
    this.service = service;
    this.autorisationService = autorisationService;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "mode-paiement-add")
  public ModePaiementDTO save(@Valid @RequestBody ModePaiementRequestDTO dto) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Ajouter un mode-paiement", "mode-paiement-add", "POST", false));
    if (service.existByCode(dto.getCode()))
      throw new APIException("Le mode de paiement avec le code " + dto.getCode() + " existe déjà");
    return service.save(dto);
  }

  @Override
  @PostMapping("/imports")
  @ResponseStatus(HttpStatus.CREATED)
  @AuthorizeUser(actionKey = "mode-paiement-import")
  public List<LiteModePaiementDTO> saveAll(@Valid @RequestBody List<ImportModePaiementRequestDTO> dtos) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Importer des mode-paiements", "mode-paiement-import", "POST", false));
    for (ImportModePaiementRequestDTO dto : dtos) {
      if (service.existByCode(dto.getCode()))
        throw new APIException("Le mode de paiement avec le code " + dto.getCode() + " existe déjà");
    }
    return service.save(dtos);
  }

  @Override
  @GetMapping("/{id}")
  @AuthorizeUser(actionKey = "mode-paiement-details")
  public ModePaiementDTO findById(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Détails d'un mode-paiement", "mode-paiement-details", "GET", false));
    return service.getOne(id);
  }

  @Override
  @DeleteMapping("/{id}")
  @AuthorizeUser(actionKey = "mode-paiement-delet")
  public void delete(@PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Supprimer un mode-paiement", "mode-paiement-delet", "DELET", false));
    if (service.findById(id) == null) throw new APIException("Le mode de paiement  avec l'id " + id + " n'existe pas");
    service.deleteById(id);
  }

  @Override
  @GetMapping
  @AuthorizeUser(actionKey = "mode-paiement-list")
  public List<LiteModePaiementDTO> list() {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Lister les mode-paiements", "mode-paiement-list", "GET", false));
    return service.findAll();
  }

  @Override
  @GetMapping("/page-query")
  public Page<LiteModePaiementDTO> pageQuery(Pageable pageable) {
    return service.findAll(pageable);
  }

  @Override
  @PutMapping("/{id}")
  @AuthorizeUser(actionKey = "mode-paiement-edit")
  public void update(@Valid @RequestBody ModePaiementRequestDTO dto, @PathVariable("id") Long id) {
    autorisationService.addDroit(new SaveDroitDTO("Finances", "Modifier un mode-paiement", "mode-paiement-edit", "PUT", false));
    if (service.findById(id) == null) throw new APIException("Le mode de paiement avec l'id " + id + " n'existe pas");
    if (service.equalsByDto(dto, id))
      throw new APIException("Le mode de paiement avec les données suivante : " + dto.toString() + " existe déjà");
    service.update(dto, id);
  }
}