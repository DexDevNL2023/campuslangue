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
  BrancheDTO save(@RequestBody BrancheRequestDTO dto);

  List<LiteBrancheDTO> saveAll(@RequestBody List<ImportBrancheRequestDTO> dtos);

  BrancheDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteBrancheDTO> list();

  Page<LiteBrancheDTO> pageQuery(Pageable pageable);

  void update(@RequestBody BrancheRequestDTO dto, @PathVariable("id") Long id);
}
