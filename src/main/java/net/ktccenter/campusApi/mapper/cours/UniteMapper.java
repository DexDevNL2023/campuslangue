package net.ktccenter.campusApi.mapper.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportUniteRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.reponse.cours.UniteDTO;
import net.ktccenter.campusApi.dto.request.cours.UniteRequestDTO;
import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.NiveauService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {NiveauService.class})
public interface UniteMapper extends GenericMapper<Unite, UniteRequestDTO, UniteDTO, LiteUniteDTO, ImportUniteRequestDTO> {
    @Override
    @Mapping(source = "niveauId", target = "niveau")
    Unite asEntity(UniteRequestDTO dto);

    @Override
    @Mapping(source = "niveauCode", target = "niveau")
    List<Unite> asEntityList(List<ImportUniteRequestDTO> dtoList);
}
