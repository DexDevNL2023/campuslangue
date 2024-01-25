package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportInscriptionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.InscriptionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscriptionRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.EtudiantService;
import net.ktccenter.campusApi.service.scolarite.SessionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SessionService.class, EtudiantService.class})
public interface InscriptionMapper extends GenericMapper<Inscription, InscriptionRequestDTO, InscriptionDTO, LiteInscriptionDTO, ImportInscriptionRequestDTO> {
    @Override
    @Mapping(source = "sessionId", target = "session")
    @Mapping(source = "etudiantId", target = "etudiant")
    Inscription asEntity(InscriptionRequestDTO dto);

    @Override
    @Mapping(source = "sessionCode", target = "session")
    @Mapping(source = "etudiantMatricule", target = "etudiant")
    List<Inscription> asEntityList(List<ImportInscriptionRequestDTO> dtoList);
}
