package net.ktccenter.campusApi.controller.structure;

import net.ktccenter.campusApi.dto.importation.administration.ImportBrancheRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.reponse.administration.BrancheDTO;
import net.ktccenter.campusApi.dto.request.administration.BrancheRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BrancheController {
  public BrancheDTO save(@RequestBody BrancheRequestDTO dto);

  public List<LiteBrancheDTO> saveAll(@RequestBody List<ImportBrancheRequestDTO> dtos);

  public BrancheDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteBrancheDTO> list();

  public Page<LiteBrancheDTO> pageQuery(Pageable pageable);

  public BrancheDTO update(@RequestBody BrancheRequestDTO dto, @PathVariable("id") Long id);
}
