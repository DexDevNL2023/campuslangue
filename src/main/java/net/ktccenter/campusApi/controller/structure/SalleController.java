package net.ktccenter.campusApi.controller.structure;

import net.ktccenter.campusApi.dto.importation.administration.ImportSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.OccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleDTO;
import net.ktccenter.campusApi.dto.request.administration.OccupationSalleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SalleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SalleController {
  public SalleDTO save(@RequestBody SalleRequestDTO dto);

  public List<LiteSalleDTO> saveAll(@RequestBody List<ImportSalleRequestDTO> dtos);

  public SalleDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteSalleDTO> list();

  public Page<LiteSalleDTO> pageQuery(Pageable pageable);

  public SalleDTO update(@RequestBody SalleRequestDTO dto, @PathVariable("id") Long id);

  public OccupationSalleDTO changeOccupation(@RequestBody OccupationSalleRequestDTO dto);

  public OccupationSalleDTO getOccupationById(@PathVariable("occupationId") Long occupationId);

  public OccupationSalleDTO getOccupationByCode(@PathVariable("occupationCode") String occupationCode);

  public List<LiteOccupationSalleDTO>  getPlageDisponible(@PathVariable("salleId") Long salleId);

  public List<LiteOccupationSalleDTO>  getPlannigSalle(@PathVariable("salleId") Long salleId);
}
