package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DiplomeService.class})
public interface EtudiantMapper extends GenericMapper<Etudiant, EtudiantRequestDTO, EtudiantDTO, LiteEtudiantDTO, ImportEtudiantRequestDTO> {
    @Override
    @Mapping(source = "dernierDiplomeId", target = "dernierDiplome")
    Etudiant asEntity(EtudiantRequestDTO dto);

    @Override
    @Mapping(source = "dernierDiplomeCode", target = "dernierDiplome")
    List<Etudiant> asEntityList(List<ImportEtudiantRequestDTO> dtoList);
}
