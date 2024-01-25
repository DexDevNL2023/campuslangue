package net.ktccenter.campusApi.mapper.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.InscriptionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InscriptionService.class})
public interface ExamenMapper extends GenericMapper<Examen, ExamenRequestDTO, ExamenDTO, LiteExamenDTO, ImportExamenRequestDTO> {
    @Override
    @Mapping(source = "inscriptionId", target = "inscription")
    Examen asEntity(ExamenRequestDTO dto);

    @Override
    @Mapping(source = "inscriptionCode", target = "inscription")
    List<Examen> asEntityList(List<ImportExamenRequestDTO> dtoList);
}
