package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.BrancheService;
import net.ktccenter.campusApi.service.administration.RoleService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleService.class, BrancheService.class})
public interface UserMapper extends GenericMapper<User, UserRequestDTO, UserDTO, LiteUserDTO, ImportUserRequestDTO> {

    @Override
    //@Mapping(source = "roleId", target = "roles")
    @Mapping(source = "brancheId", target = "branche")
    User asEntity(UserRequestDTO dto);

    @Override
    @Mapping(source = "roleLibelle", target = "roles")
    @Mapping(source = "brancheCode", target = "branche")
    List<User> asEntityList(List<ImportUserRequestDTO> dtoList);
}
