package net.ktccenter.campusApi.controller.administration.impl;


import net.ktccenter.campusApi.controller.administration.InstitutionController;
import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.service.administration.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/administration/institutions")
@RestController
@CrossOrigin("*")
public class InstitutionControllerImpl implements InstitutionController {
    private final InstitutionService service;

    public InstitutionControllerImpl(InstitutionService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstitutionDTO save(@Valid @RequestBody InstitutionRequestDTO dto) {
        return service.save(dto);
    }

    @Override
    @GetMapping("/parametres")
    public InstitutionDTO getInstitution() {
        return service.getCurrentInstitution();
    }


    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody InstitutionRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null)
            throw new RuntimeException("L'institution avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("L'institution avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }
}
