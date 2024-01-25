package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Compte;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.InscriptionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InscriptionService.class})
public interface CompteMapper extends GenericMapper<Compte, CompteRequestDTO, CompteDTO, LiteCompteDTO, ImportCompteRequestDTO> {
    @Override
    @Mapping(source = "inscriptionId", target = "inscription")
    Compte asEntity(CompteRequestDTO dto);

    @Override
    @Mapping(source = "inscriptionCode", target = "inscription")
    List<Compte> asEntityList(List<ImportCompteRequestDTO> dtoList);
}
