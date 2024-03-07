package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportJourOuvrableRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.JourOuvrableDTO;
import net.ktccenter.campusApi.dto.request.administration.JourOuvrableRequestDTO;
import net.ktccenter.campusApi.entities.administration.JourOuvrable;
import net.ktccenter.campusApi.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JourOuvrableMapper extends GenericMapper<JourOuvrable, JourOuvrableRequestDTO, JourOuvrableDTO, LiteJourOuvrableDTO, ImportJourOuvrableRequestDTO> {
}
