package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SessionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface SessionService extends GenericService<Session, SessionRequestDTO, SessionDTO, LiteSessionDTO, ImportSessionRequestDTO> {

  boolean equalsByDto(SessionRequestDTO dto, Long id);

  Session findByCode(String code);

  boolean existByCode(String code);

    List<SessionBranchDTO> findAll();

  SessionDTO close(Long id);

  List<LiteSessionForNoteDTO> getAllSessionByCurrentUser();
}
