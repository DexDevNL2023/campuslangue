package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportInscriptionRequestDTO;
import net.ktccenter.campusApi.dto.lite.LiteNewInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.InscriptionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.InscriptionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscriptionRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireExitStudientRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireNewStudientRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.service.GenericService;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

public interface InscriptionService extends GenericService<Inscription, InscriptionRequestDTO, InscriptionDTO, LiteInscriptionDTO, ImportInscriptionRequestDTO> {

    List<InscriptionBranchDTO> findAll();

    boolean equalsByDto(InscriptionRequestDTO dto, Long id);

    LiteNewInscriptionDTO inscrireExitStudient(InscrireExitStudientRequestDTO dto);

    LiteNewInscriptionDTO inscrireNewStudient(InscrireNewStudientRequestDTO dto);

  Path downloadAttestation(Long id) throws URISyntaxException, JRException, IOException;
}
