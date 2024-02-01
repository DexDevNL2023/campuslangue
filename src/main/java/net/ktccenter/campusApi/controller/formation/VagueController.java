package net.ktccenter.campusApi.controller.formation;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface VagueController {
  VagueDTO save(@RequestBody VagueRequestDTO dto);

  List<LiteVagueDTO> saveAll(@RequestBody List<ImportVagueRequestDTO> dtos);

  VagueDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  //List<VagueBranchDTO> list();
  List<LiteVagueDTO> list();

  Page<LiteVagueDTO> pageQuery(Pageable pageable);

  void update(@RequestBody VagueRequestDTO dto, @PathVariable("id") Long id);
}
