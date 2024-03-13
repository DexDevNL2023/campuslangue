package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.NiveauController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportNiveauRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.NiveauDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.NiveauRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
import net.ktccenter.campusApi.service.scolarite.NiveauService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/scolarite/niveaux")
@RestController
@CrossOrigin("*")
public class NiveauControllerImpl implements NiveauController {
    private final NiveauService service;

    private final DiplomeService diplomeService;
    private final AutorisationService autorisationService;

    public NiveauControllerImpl(NiveauService service, DiplomeService diplomeService, AutorisationService autorisationService) {
        this.service = service;
        this.diplomeService = diplomeService;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "niveau-add")
    public NiveauDTO save(@Valid @RequestBody NiveauRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Ajouter un niveau", "niveau-add", "POST", false));
        if (service.existByCode(dto.getCode()))
            throw new APIException("Le niveau avec le code " + dto.getCode() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "niveau-import")
    public List<LiteNiveauDTO> saveAll(@Valid @RequestBody List<ImportNiveauRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Importer des niveaus", "niveau-import", "POST", false));
        for (ImportNiveauRequestDTO dto : dtos) {
            if (service.existByCode(dto.getCode()))
                throw new APIException("Le niveau avec le code " + dto.getCode() + " existe déjà");

            if (!diplomeService.existByCode(dto.getDiplomeRequisCode())) {
                throw new ResourceNotFoundException("Diplôme réquis avec le code " + dto.getDiplomeRequisCode() + " n'existe pas!");
            }
            if (!diplomeService.existByCode(dto.getDiplomeFinFormationCode())) {
                throw new ResourceNotFoundException("Diplôme de fin de formation avec le code " + dto.getDiplomeFinFormationCode() + " n'existe pas!");
            }
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    @AuthorizeUser(actionKey = "niveau-details")
    public NiveauDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Détails d'un niveau", "niveau-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @AuthorizeUser(actionKey = "niveau-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Supprimer un niveau", "niveau-delet", "DELET", false));
        if (service.findById(id) == null) throw new APIException("Le niveau  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    @AuthorizeUser(actionKey = "niveau-list")
    public List<LiteNiveauDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les niveaus", "niveau-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteNiveauDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    @AuthorizeUser(actionKey = "niveau-edit")
    public void update(@Valid @RequestBody NiveauRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Modifier un niveau", "niveau-edit", "PUT", false));
        if (service.findById(id) == null) throw new APIException("Le niveau avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("Le niveau avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
