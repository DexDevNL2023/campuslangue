package net.ktccenter.campusApi.controller.structure;

import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusByBranchDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CampusBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CampusController {
  CampusDTO save(@RequestBody CampusRequestDTO dto);

  List<LiteCampusDTO> saveAll(@RequestBody List<ImportCampusRequestDTO> dtos);

  CampusDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<CampusBranchDTO> list();

    List<CampusByBranchDTO> listByBranch(@PathVariable("branchId") Long branchId);

  Page<LiteCampusDTO> pageQuery(Pageable pageable);

  void update(@RequestBody CampusRequestDTO dto, @PathVariable("id") Long id);
}
