package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiplomeMapper extends GenericMapper<Diplome, DiplomeRequestDTO, DiplomeDTO, LiteDiplomeDTO, ImportDiplomeRequestDTO> {
}
