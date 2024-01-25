package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface ExamenService extends GenericService<Examen, ExamenRequestDTO, ExamenDTO, LiteExamenDTO, ImportExamenRequestDTO> {
  boolean existsByCode(String code);

  boolean equalsByDto(ExamenRequestDTO dto, Long id);

  Examen findByCode(String code);

  List<ExamenForNoteReponseDTO> getAllExamenBySession(Long sessionId, Long uniteId);

  List<ExamenForNoteReponseDTO> saisieNotesexamen(List<ExamenForNoteDTO> dtos);
}
