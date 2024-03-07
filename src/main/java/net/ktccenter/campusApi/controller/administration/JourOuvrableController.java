package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.JourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RequestJourOuvrableDTO;
import net.ktccenter.campusApi.dto.request.administration.JourOuvrableRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface JourOuvrableController {
  JourOuvrableDTO save(@RequestBody JourOuvrableRequestDTO dto);

  JourOuvrableDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteJourOuvrableDTO> list();

  Page<LiteJourOuvrableDTO> pageQuery(Pageable pageable);

  void update(@RequestBody JourOuvrableRequestDTO dto, @PathVariable("id") Long id);

  List<RequestJourOuvrableDTO> getDefaultJour();

  List<JourOuvrableDTO> saveAll(List<JourOuvrableRequestDTO> dtos);
}
