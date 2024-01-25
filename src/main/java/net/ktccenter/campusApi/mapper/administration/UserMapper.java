package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.RoleService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

    @Mapper(componentModel = "spring", uses = {RoleService.class})
public interface UserMapper extends GenericMapper<User, UserRequestDTO, UserDTO, LiteUserDTO, ImportUserRequestDTO> {

    @Override
    @Mapping(source = "roleIds", target = "roles")
    User asEntity(UserRequestDTO dto);

    @Override
    @Mapping(source = "roleLibelles", target = "roles")
    List<User> asEntityList(List<ImportUserRequestDTO> dtoList);
}
