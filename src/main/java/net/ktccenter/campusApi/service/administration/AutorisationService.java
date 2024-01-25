package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDetailsDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;

import java.util.List;

public interface AutorisationService {
    List<RoleDTO> findAll();
    RoleDetailsDTO getOne(Long id);
    void save(RoleDTO roleDTO);
    void update(RoleDTO roleDTO);
    void changeAutorisation(PermissionDTO permissionDTO);
    void changeIsDefaultDroit(DroitDTO droitDTO);
    List<ModuleDTO> allModules();
    void addDroit(SaveDroitDTO saveDroitDTO);
}


