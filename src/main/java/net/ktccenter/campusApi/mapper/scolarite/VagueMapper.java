package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Vague;
import net.ktccenter.campusApi.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface VagueMapper extends GenericMapper<Vague, VagueRequestDTO, VagueDTO, LiteVagueDTO, ImportVagueRequestDTO> {
}
