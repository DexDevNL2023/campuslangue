package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementForCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchAndCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.PaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.PaiementRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface PaiementService extends GenericService<Paiement, PaiementRequestDTO, PaiementDTO, LitePaiementDTO, ImportPaiementRequestDTO> {

  boolean equalsByDto(PaiementRequestDTO dto, Long id);

  Paiement findByRefPaiement(String code);

    List<PaiementBranchDTO> findAll();

  boolean existByRefPaiement(String code);

    List<LitePaiementForCampusDTO> findAllByCampus(Long campusId);

    List<PaiementBranchAndCampusDTO> findAllAndGroupByBranchAndCampus();
}
