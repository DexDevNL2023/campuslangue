package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface FormateurController {
  public FormateurDTO save(@RequestBody FormateurRequestDTO dto);

  public List<LiteFormateurDTO> saveAll(@RequestBody List<ImportFormateurRequestDTO> dtos);

  public FormateurDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteFormateurDTO> list();

  public Page<LiteFormateurDTO> pageQuery(Pageable pageable);

  public FormateurDTO update(@RequestBody FormateurRequestDTO dto, @PathVariable("id") Long id);
}
