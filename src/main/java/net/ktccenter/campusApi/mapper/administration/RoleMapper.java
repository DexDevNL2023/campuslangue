package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportRoleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface RoleMapper extends GenericMapper<Role, RoleRequestDTO, RoleDTO, LiteRoleDTO, ImportRoleRequestDTO> {
}
