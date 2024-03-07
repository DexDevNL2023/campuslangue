package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportJourOuvrableRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.JourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RequestJourOuvrableDTO;
import net.ktccenter.campusApi.dto.request.administration.JourOuvrableRequestDTO;
import net.ktccenter.campusApi.entities.administration.JourOuvrable;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface JourOuvrableService extends GenericService<JourOuvrable, JourOuvrableRequestDTO, JourOuvrableDTO, LiteJourOuvrableDTO, ImportJourOuvrableRequestDTO> {
    boolean equalsByDto(JourOuvrableRequestDTO dto, Long id);

    List<LiteJourOuvrableDTO> findAll();

    List<RequestJourOuvrableDTO> getDefaultJour();

    List<JourOuvrableDTO> saveAll(List<JourOuvrableRequestDTO> dtos);
}
