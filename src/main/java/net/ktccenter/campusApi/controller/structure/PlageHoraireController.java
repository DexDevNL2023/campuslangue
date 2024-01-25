package net.ktccenter.campusApi.controller.structure;

import net.ktccenter.campusApi.dto.importation.cours.ImportPlageHoraireRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageHoraireDTO;
import net.ktccenter.campusApi.dto.request.cours.PlageHoraireRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PlageHoraireController {
  public PlageHoraireDTO save(@RequestBody PlageHoraireRequestDTO dto);

  public List<LitePlageHoraireDTO> saveAll(@RequestBody List<ImportPlageHoraireRequestDTO> dtos);

  public PlageHoraireDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LitePlageHoraireDTO> list();

  public Page<LitePlageHoraireDTO> pageQuery(Pageable pageable);

  public PlageHoraireDTO update(@RequestBody PlageHoraireRequestDTO dto, @PathVariable("id") Long id);
}
