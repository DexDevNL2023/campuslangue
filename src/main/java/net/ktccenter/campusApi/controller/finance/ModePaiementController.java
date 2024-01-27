package net.ktccenter.campusApi.controller.finance;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportModePaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModePaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModePaiementRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ModePaiementController {
  ModePaiementDTO save(@RequestBody ModePaiementRequestDTO dto);

  List<LiteModePaiementDTO> saveAll(@RequestBody List<ImportModePaiementRequestDTO> dtos);

  ModePaiementDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteModePaiementDTO> list();

  Page<LiteModePaiementDTO> pageQuery(Pageable pageable);

  void update(@RequestBody ModePaiementRequestDTO dto, @PathVariable("id") Long id);
}
