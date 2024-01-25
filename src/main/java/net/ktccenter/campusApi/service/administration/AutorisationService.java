package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;

public interface AutorisationService {
    void changeAutorisation(PermissionDTO permissionDTO);
    void changeIsDefaultDroit(DroitDTO droitDTO);
    void addDroit(SaveDroitDTO saveDroitDTO);
}


