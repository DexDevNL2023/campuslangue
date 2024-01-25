package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportModePaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModePaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModePaiementRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.ModePaiement;
import net.ktccenter.campusApi.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface ModePaiementMapper extends GenericMapper<ModePaiement, ModePaiementRequestDTO, ModePaiementDTO, LiteModePaiementDTO, ImportModePaiementRequestDTO> {
}
