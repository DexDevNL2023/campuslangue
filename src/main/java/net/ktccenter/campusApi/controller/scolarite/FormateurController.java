package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.branch.FormateurBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FormateurController {
  FormateurDTO save(@RequestBody FormateurRequestDTO dto);

  List<LiteFormateurDTO> saveAll(@RequestBody List<ImportFormateurRequestDTO> dtos);

  FormateurDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<FormateurBranchDTO> list();

  List<LiteFormateurDTO> listByBranch(@PathVariable("branchId") Long branchId);

  Page<LiteFormateurDTO> pageQuery(Pageable pageable);

  void update(@RequestBody FormateurRequestDTO dto, @PathVariable("id") Long id);
}
