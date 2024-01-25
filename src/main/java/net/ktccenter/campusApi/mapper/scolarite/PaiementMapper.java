package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.PaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.PaiementRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.CompteService;
import net.ktccenter.campusApi.service.scolarite.ModePaiementService;
import net.ktccenter.campusApi.service.scolarite.RubriqueService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ModePaiementService.class, RubriqueService.class, CompteService.class})
public interface PaiementMapper extends GenericMapper<Paiement, PaiementRequestDTO, PaiementDTO, LitePaiementDTO, ImportPaiementRequestDTO> {
    @Override
    @Mapping(source = "modePaiementId", target = "modePaiement")
    @Mapping(source = "rubriqueId", target = "rubrique")
    @Mapping(source = "compteId", target = "compte")
    Paiement asEntity(PaiementRequestDTO dto);

    @Override
    @Mapping(source = "modePaiementCode", target = "modePaiement")
    @Mapping(source = "rubriqueCode", target = "rubrique")
    @Mapping(source = "compteCode", target = "compte")
    List<Paiement> asEntityList(List<ImportPaiementRequestDTO> dtoList);
}
