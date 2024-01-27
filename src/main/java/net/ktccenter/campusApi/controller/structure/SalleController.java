package net.ktccenter.campusApi.controller.structure;

import net.ktccenter.campusApi.dto.importation.administration.ImportSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.OccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SalleBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.OccupationSalleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SalleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SalleController {
  SalleDTO save(@RequestBody SalleRequestDTO dto);

  List<LiteSalleDTO> saveAll(@RequestBody List<ImportSalleRequestDTO> dtos);

  SalleDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<SalleBranchDTO> list();

  Page<LiteSalleDTO> pageQuery(Pageable pageable);

  void update(@RequestBody SalleRequestDTO dto, @PathVariable("id") Long id);

  OccupationSalleDTO changeOccupation(@RequestBody OccupationSalleRequestDTO dto);

  OccupationSalleDTO getOccupationById(@PathVariable("occupationId") Long occupationId);

  OccupationSalleDTO getOccupationByCode(@PathVariable("occupationCode") String occupationCode);

  List<LiteOccupationSalleDTO> getPlageDisponible(@PathVariable("salleId") Long salleId);

  List<LiteOccupationSalleDTO> getPlannigSalle(@PathVariable("salleId") Long salleId);
}
