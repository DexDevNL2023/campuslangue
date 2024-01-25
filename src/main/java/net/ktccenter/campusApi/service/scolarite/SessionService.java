package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.service.GenericService;

public interface SessionService extends GenericService<Session, SessionRequestDTO, SessionDTO, LiteSessionDTO, ImportSessionRequestDTO> {

  boolean equalsByDto(SessionRequestDTO dto, Long id);

  Session findByCode(String code);

  boolean existByCode(String code);

  SessionDTO close(Long id);
}
