package net.ktccenter.campusApi.controller.formation;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SessionController {
  SessionDTO save(@RequestBody SessionRequestDTO dto);

  List<LiteSessionDTO> saveAll(@RequestBody List<ImportSessionRequestDTO> dtos);

  SessionDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteSessionDTO> list();

  Page<LiteSessionDTO> pageQuery(Pageable pageable);

  void update(@RequestBody SessionRequestDTO dto, @PathVariable("id") Long id);

  SessionDTO close(@PathVariable("id") Long id);
}