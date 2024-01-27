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
  PlageHoraireDTO save(@RequestBody PlageHoraireRequestDTO dto);

  List<LitePlageHoraireDTO> saveAll(@RequestBody List<ImportPlageHoraireRequestDTO> dtos);

  PlageHoraireDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LitePlageHoraireDTO> list();

  Page<LitePlageHoraireDTO> pageQuery(Pageable pageable);

  void update(@RequestBody PlageHoraireRequestDTO dto, @PathVariable("id") Long id);
}
