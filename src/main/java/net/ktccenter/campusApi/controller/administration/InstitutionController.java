package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface InstitutionController {
    public InstitutionDTO save(@RequestBody InstitutionRequestDTO institution);

    public InstitutionDTO getInstitution();

    public InstitutionDTO update(@RequestBody InstitutionRequestDTO dto, @PathVariable("id") Long id);
}
