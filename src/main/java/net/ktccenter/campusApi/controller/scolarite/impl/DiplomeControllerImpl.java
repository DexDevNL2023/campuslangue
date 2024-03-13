package net.ktccenter.campusApi.controller.scolarite.impl;

import net.ktccenter.campusApi.controller.scolarite.DiplomeController;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/scolarite/diplomes")
@RestController
@CrossOrigin("*")
public class DiplomeControllerImpl implements DiplomeController {
    private final DiplomeService service;
    private final AutorisationService autorisationService;

    public DiplomeControllerImpl(DiplomeService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "diplome-add")
    public DiplomeDTO save(@Valid @RequestBody DiplomeRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Ajouter un diplôme", "diplome-add", "POST", false));
        if (service.existByCode(dto.getCode()))
            throw new APIException("Le diplôme avec le code " + dto.getCode() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "diplome-import")
    public List<LiteDiplomeDTO> saveAll(@Valid @RequestBody List<ImportDiplomeRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Importer des diplômes", "diplome-import", "POST", false));
        for (ImportDiplomeRequestDTO dto : dtos) {
            if (service.existByCode(dto.getCode()))
                throw new ResourceNotFoundException("Le diplôme avec le code " + dto.getCode() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    @AuthorizeUser(actionKey = "diplome-details")
    public DiplomeDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Détails d'un diplôme", "diplome-details", "GET", false));
        return service.getOne(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @AuthorizeUser(actionKey = "diplome-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Supprimer un diplôme", "diplome-delet", "DELET", false));
        if (service.findById(id) == null) throw new APIException("Le diplôme  avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    @AuthorizeUser(actionKey = "diplome-list")
    public List<LiteDiplomeDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Lister les diplômes", "diplome-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteDiplomeDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    @AuthorizeUser(actionKey = "diplome-edit")
    public DiplomeDTO update(@Valid @RequestBody DiplomeRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Scolarité", "Modifier un diplôme", "diplome-edit", "PUT", false));
        if (service.findById(id) == null) throw new APIException("Le diplôme avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new APIException("Le diplôme avec les données suivante : " + dto.toString() + " existe déjà");
        return service.update(dto, id);
    }
}
