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
  public VagueDTO save(@RequestBody VagueRequestDTO dto);

  public List<LiteVagueDTO> saveAll(@RequestBody List<ImportVagueRequestDTO> dtos);

  public VagueDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteVagueDTO> list();

  public Page<LiteVagueDTO> pageQuery(Pageable pageable);

  public VagueDTO update(@RequestBody VagueRequestDTO dto, @PathVariable("id") Long id);
}
