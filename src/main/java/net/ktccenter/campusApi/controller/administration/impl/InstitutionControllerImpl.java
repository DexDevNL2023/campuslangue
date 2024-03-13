package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.InstitutionController;
import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.ParametreInstitutionRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.InstitutionService;
import net.ktccenter.campusApi.validators.AuthorizeUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/administration/institutions")
@RestController
@CrossOrigin("*")
public class InstitutionControllerImpl implements InstitutionController {
    private final InstitutionService service;
    private final AutorisationService autorisationService;

    public InstitutionControllerImpl(InstitutionService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AuthorizeUser(actionKey = "institution-add")
    public InstitutionDTO save(@Valid @RequestBody InstitutionRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Ajouter une institution", "institution-add", "POST", false));
        return service.save(dto);
    }

    @Override
    @GetMapping("/parametres")
    @AuthorizeUser(actionKey = "institution-details")
    public InstitutionDTO getInstitution() {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Détails d'une institution", "institution-details", "GET", false));
        return service.getCurrentInstitution();
    }


    @Override
    @PutMapping("/{id}")
    @AuthorizeUser(actionKey = "institution-edit")
    public void update(@Valid @RequestBody InstitutionRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Modifier une institution", "institution-edit", "PUT", false));
        if (service.findById(id) == null)
            throw new RuntimeException("L'institution avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("L'institution avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }


    @Override
    @PutMapping("/update/parameters")
    @AuthorizeUser(actionKey = "institution-edit-parametres")
    public void updateParametres(@Valid @RequestBody ParametreInstitutionRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("Administration", "Modifier les paramètres d'une institution", "institution-edit-parametres", "PUT", false));
        service.updateParametres(dto);
    }
}
