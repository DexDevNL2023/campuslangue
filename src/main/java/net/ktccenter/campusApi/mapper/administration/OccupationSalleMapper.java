package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportOccupationSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.OccupationSalleDTO;
import net.ktccenter.campusApi.dto.request.administration.OccupationSalleRequestDTO;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.SalleService;
import net.ktccenter.campusApi.service.cours.PlageHoraireService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PlageHoraireService.class, SalleService.class})
public interface OccupationSalleMapper extends GenericMapper<OccupationSalle, OccupationSalleRequestDTO, OccupationSalleDTO, LiteOccupationSalleDTO, ImportOccupationSalleRequestDTO> {
    @Override
    @Mapping(source = "plageHoraireId", target = "plageHoraire")
    @Mapping(source = "salleId", target = "salle")
    OccupationSalle asEntity(OccupationSalleRequestDTO dto);

    @Override
    @Mapping(source = "plageHoraireCode", target = "plageHoraire")
    @Mapping(source = "salleCode", target = "salle")
    List<OccupationSalle> asEntityList(List<ImportOccupationSalleRequestDTO> dtoList);
}
