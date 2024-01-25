package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportRoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleDroitRequestDTO;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.service.administration.DroitService;
import net.ktccenter.campusApi.service.administration.RoleService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleService.class, DroitService.class})
public interface RoleDroitMapper extends GenericMapper<RoleDroit, RoleDroitRequestDTO, RoleDroitDTO, LiteRoleDroitDTO, ImportRoleDroitRequestDTO> {
    @Override
    @Mapping(source = "roleId", target = "role")
    @Mapping(source = "droitId", target = "droit")
    RoleDroit asEntity(RoleDroitRequestDTO dto);

    @Override
    @Mapping(source = "roleLibelle", target = "role")
    @Mapping(source = "droitLibelle", target = "droit")
    List<RoleDroit> asEntityList(List<ImportRoleDroitRequestDTO> dtoList);
}
