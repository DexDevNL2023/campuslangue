package net.ktccenter.campusApi.mapper.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.cours.ExamenService;
import net.ktccenter.campusApi.service.cours.UniteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UniteService.class, ExamenService.class})
public interface EpreuveMapper extends GenericMapper<Epreuve, EpreuveRequestDTO, EpreuveDTO, LiteEpreuveDTO, ImportEpreuveRequestDTO> {
    @Override
    @Mapping(source = "uniteId", target = "unite")
    @Mapping(source = "examenId", target = "examen")
    Epreuve asEntity(EpreuveRequestDTO dto);

    @Override
    @Mapping(source = "uniteCode", target = "unite")
    @Mapping(source = "examenCode", target = "examen")
    List<Epreuve> asEntityList(List<ImportEpreuveRequestDTO> dtoList);
}
