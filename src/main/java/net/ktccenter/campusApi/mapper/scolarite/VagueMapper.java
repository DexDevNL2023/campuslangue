package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Vague;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.BrancheService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BrancheService.class})
public interface VagueMapper extends GenericMapper<Vague, VagueRequestDTO, VagueDTO, LiteVagueDTO, ImportVagueRequestDTO> {
    @Override
    @Mapping(source = "brancheId", target = "branche")
    Vague asEntity(VagueRequestDTO dto);

    @Override
    @Mapping(source = "brancheCode", target = "branche")
    List<Vague> asEntityList(List<ImportVagueRequestDTO> dtoList);
}
