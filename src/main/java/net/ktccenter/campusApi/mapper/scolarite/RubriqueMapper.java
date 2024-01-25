package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportRubriqueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import net.ktccenter.campusApi.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RubriqueMapper extends GenericMapper<Rubrique, RubriqueRequestDTO, RubriqueDTO, LiteRubriqueDTO, ImportRubriqueRequestDTO> {
}
