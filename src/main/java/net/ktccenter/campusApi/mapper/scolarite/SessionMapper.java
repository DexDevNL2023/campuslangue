package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionForNoteDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.BrancheService;
import net.ktccenter.campusApi.service.scolarite.FormateurService;
import net.ktccenter.campusApi.service.scolarite.NiveauService;
import net.ktccenter.campusApi.service.scolarite.VagueService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BrancheService.class, NiveauService.class, VagueService.class, FormateurService.class})
public interface SessionMapper extends GenericMapper<Session, SessionRequestDTO, SessionDTO, LiteSessionDTO, ImportSessionRequestDTO> {
    @Override
    @Mapping(source = "brancheId", target = "branche")
    @Mapping(source = "niveauId", target = "niveau")
    @Mapping(source = "vagueId", target = "vague")
    @Mapping(source = "formateurId", target = "formateur")
    Session asEntity(SessionRequestDTO dto);

    @Override
    @Mapping(source = "brancheCode", target = "branche")
    @Mapping(source = "niveauCode", target = "niveau")
    @Mapping(source = "vagueCode", target = "vague")
    @Mapping(source = "formateurMatricule", target = "formateur")
    List<Session> asEntityList(List<ImportSessionRequestDTO> dtoList);

    @Mapping(source = "brancheCode", target = "branche")
    @Mapping(source = "niveauCode", target = "niveau")
    @Mapping(source = "vagueCode", target = "vague")
    @Mapping(source = "formateurMatricule", target = "formateur")
    Session asEntity(ImportSessionRequestDTO dto);

    LiteSessionForNoteDTO asLiteDto(Session entity);

    List<LiteSessionForNoteDTO> asLiteDtoList(List<Session> entityList);
}
