package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.ParametreInstitutionRequestDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface InstitutionController {
    InstitutionDTO save(@RequestBody InstitutionRequestDTO institution);

    InstitutionDTO getInstitution();

    void update(@RequestBody InstitutionRequestDTO dto, @PathVariable("id") Long id);

    void updateParametres(@Valid @RequestBody ParametreInstitutionRequestDTO dto);
}
