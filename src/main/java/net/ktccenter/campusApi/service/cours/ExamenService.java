package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.reponse.branch.ExamenBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForResultatReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import net.ktccenter.campusApi.dto.request.cours.FullExamen2ForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.FullExamenForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.FullExamenForNoteImportDTO;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.enums.ResultatState;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface ExamenService extends GenericService<Examen, ExamenRequestDTO, ExamenDTO, LiteExamenDTO, ImportExamenRequestDTO> {
    List<ExamenBranchDTO> findAll();

    boolean existsByCode(String code);

    boolean equalsByDto(ExamenRequestDTO dto, Long id);

    Examen findByCode(String code);

    FullExamenForNoteDTO getAllExamenBySessionAndUnite(Long sessionId, Long uniteId);

    List<ExamenForNoteReponseDTO> saisieNotesExamen(FullExamenForNoteDTO dto);

    FullExamen2ForNoteDTO getAllExamenBySession(Long sessionId);

    List<ExamenForNoteReponseDTO> saisieNotesExamen2(FullExamen2ForNoteDTO dto);

    void importNotesExamen(FullExamenForNoteImportDTO dto);

    FullExamenForNoteDTO getAllNoteStudentIsRattapage(Long sessionId, Long uniteId);

    List<ExamenForResultatReponseDTO> getAllResultatExamenBySession(Long sessionId, ResultatState state);
}
