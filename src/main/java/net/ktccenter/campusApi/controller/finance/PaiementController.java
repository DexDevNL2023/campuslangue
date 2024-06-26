package net.ktccenter.campusApi.controller.finance;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementForCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchAndCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.PaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.PaiementRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PaiementController {
  PaiementDTO save(@RequestBody PaiementRequestDTO dto);

  List<LitePaiementDTO> saveAll(@RequestBody List<ImportPaiementRequestDTO> dtos);

  PaiementDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

    List<PaiementBranchDTO> list();

    List<PaiementBranchAndCampusDTO> listAllAndGroupByBranchAndCampus();

  Page<LitePaiementDTO> pageQuery(Pageable pageable);

  void update(@RequestBody PaiementRequestDTO dto, @PathVariable("id") Long id);

    List<LitePaiementForCampusDTO> listByCampus(@PathVariable("campusId") Long campusId);
}
