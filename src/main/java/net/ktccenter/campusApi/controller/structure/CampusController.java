package net.ktccenter.campusApi.controller.structure;

import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CampusController {
  public CampusDTO save(@RequestBody CampusRequestDTO dto);

  public List<LiteCampusDTO> saveAll(@RequestBody List<ImportCampusRequestDTO> dtos);

  public CampusDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteCampusDTO> list();

  public Page<LiteCampusDTO> pageQuery(Pageable pageable);

  public CampusDTO update(@RequestBody CampusRequestDTO dto, @PathVariable("id") Long id);
}
