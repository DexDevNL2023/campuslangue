package net.ktccenter.campusApi.controller.finance.impl;

import net.ktccenter.campusApi.controller.finance.CompteController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CompteBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.CompteService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/finance/comptes")
@RestController
@CrossOrigin("*")
public class CompteControllerImpl implements CompteController {
    private final CompteService service;
    private final AutorisationService autorisationService;

    public CompteControllerImpl(CompteService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "compte-add")
    public CompteDTO save(@Valid @RequestBody CompteRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Finances", "Ajouter un compte de paiement", "compte-add", "POST", false));
        if (service.existByCode(dto.getCode()))
            throw new APIException("Le compte de paiement avec le code " + dto.getCode() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "compte-import")
    public List<LiteCompteDTO> saveAll(@Valid @RequestBody List<ImportCompteRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("Finances", "Importer des comptes de paiement", "compte-import", "POST", false));
        for (ImportCompteRequestDTO dto : dtos) {
            if (service.existByCode(dto.getCode()))
                throw new APIException("Le compte de paiement avec le code " + dto.getCode() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    @AuthorizeUser(actionKey = "compte-details")
    public CompteDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Finances", "Détails d'un compte de paiement", "compte-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @AuthorizeUser(actionKey = "compte-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Finances", "Supprimer un compte de paiement", "compte-delet", "DELET", false));
        if (service.findById(id) == null)
            throw new APIException("Le compte de paiement  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    @AuthorizeUser(actionKey = "compte-list")
    public List<CompteBranchDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("Finances", "Lister les comptes de paiement", "compte-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteCompteDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    @AuthorizeUser(actionKey = "compte-edit")
    public void update(@Valid @RequestBody CompteRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Finances", "Modifier un compte de paiement", "compte-edit", "PUT", false));
        if (service.findById(id) == null)
            throw new APIException("Le compte de paiement avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("Le compte de paiement avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}