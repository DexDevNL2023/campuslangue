package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.lite.administration.LitePermissionAndModuleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LitePermissionModuleAccesStatusDTO;
import net.ktccenter.campusApi.dto.lite.administration.LitePermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;

import java.util.List;

public interface AutorisationService {
    void changeAutorisation(PermissionDTO permissionDTO);
    void changeIsDefaultDroit(DroitDTO droitDTO);
    void addDroit(SaveDroitDTO saveDroitDTO);
    List<LitePermissionModuleAccesStatusDTO> getRolePersmission(String roleName);
    List<LitePermissionModuleDTO> getAllPermissions();

    LitePermissionAndModuleDTO getAllPermissionsByRole(String roleName);
}


