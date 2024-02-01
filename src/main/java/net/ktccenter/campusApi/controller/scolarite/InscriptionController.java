package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportInscriptionRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.InscriptionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.InscriptionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscriptionRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireExitStudientRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireNewStudientRequestDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface InscriptionController {
  LiteInscriptionDTO inscrireExitStudient(@RequestBody InscrireExitStudientRequestDTO dto);

  LiteInscriptionDTO inscrireNewStudient(@RequestBody InscrireNewStudientRequestDTO dto);
  InscriptionDTO save(@RequestBody InscriptionRequestDTO dto);

  List<LiteInscriptionDTO> saveAll(@RequestBody List<ImportInscriptionRequestDTO> dtos);

  InscriptionDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

    List<InscriptionBranchDTO> list();

  Page<LiteInscriptionDTO> pageQuery(Pageable pageable);

  void update(@RequestBody InscriptionRequestDTO dto, @PathVariable("id") Long id);

  void download(@PathVariable("inscriptionId") Long inscriptionId, HttpServletResponse response) throws URISyntaxException, JRException, IOException;
}
