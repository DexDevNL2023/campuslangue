package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.BrancheService;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DiplomeService.class, BrancheService.class})
public interface FormateurMapper extends GenericMapper<Formateur, FormateurRequestDTO, FormateurDTO, LiteFormateurDTO, ImportFormateurRequestDTO> {
    @Override
    @Mapping(source = "diplomeId", target = "diplome")
    @Mapping(source = "brancheId", target = "branche")
    Formateur asEntity(FormateurRequestDTO dto);

    @Override
    @Mapping(source = "diplomeCode", target = "diplome")
    @Mapping(source = "brancheCode", target = "branche")
    List<Formateur> asEntityList(List<ImportFormateurRequestDTO> dtoList);

    @Mapping(source = "diplomeCode", target = "diplome")
    @Mapping(source = "brancheCode", target = "branche")
    Formateur asEntity(ImportFormateurRequestDTO dto);
}
