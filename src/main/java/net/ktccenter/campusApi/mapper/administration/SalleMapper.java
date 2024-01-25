package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleDTO;
import net.ktccenter.campusApi.dto.request.administration.SalleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Salle;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.CampusService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CampusService.class})
public interface SalleMapper extends GenericMapper<Salle, SalleRequestDTO, SalleDTO, LiteSalleDTO, ImportSalleRequestDTO> {
  @Override
  @Mapping(source = "campusId", target = "campus")
  Salle asEntity(SalleRequestDTO dto);

  @Override
  @Mapping(source = "campusCode", target = "campus")
  List<Salle> asEntityList(List<ImportSalleRequestDTO> dtoList);
}
