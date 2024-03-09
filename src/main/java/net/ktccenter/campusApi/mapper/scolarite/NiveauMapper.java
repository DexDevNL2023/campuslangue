package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportNiveauRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.NiveauDTO;
import net.ktccenter.campusApi.dto.request.scolarite.NiveauRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DiplomeService.class})
public interface NiveauMapper extends GenericMapper<Niveau, NiveauRequestDTO, NiveauDTO, LiteNiveauDTO, ImportNiveauRequestDTO> {
    @Override
    @Mapping(source = "diplomeRequisId", target = "diplomeRequis")
    @Mapping(source = "diplomeFinFormationId", target = "diplomeFinFormation")
    Niveau asEntity(NiveauRequestDTO dto);

    @Override
    @Mapping(source = "diplomeRequisCode", target = "diplomeRequis")
    @Mapping(source = "diplomeFinFormationCode", target = "diplomeFinFormation")
    List<Niveau> asEntityList(List<ImportNiveauRequestDTO> dtoList);

    @Mapping(source = "diplomeRequisCode", target = "diplomeRequis")
    @Mapping(source = "diplomeFinFormationCode", target = "diplomeFinFormation")
    Niveau asEntity(ImportNiveauRequestDTO dto);
}
