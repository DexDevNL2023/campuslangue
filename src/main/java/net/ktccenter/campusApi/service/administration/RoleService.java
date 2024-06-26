package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportRoleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface RoleService extends GenericService<Role, RoleRequestDTO, RoleDTO, LiteRoleDTO, ImportRoleRequestDTO> {
    List<LiteRoleDTO> findAll();

    boolean existsByRoleName(String libelle);

    boolean equalsByDto(RoleRequestDTO dto, Long id);

    Role findByLibelle(String libelle);
}
