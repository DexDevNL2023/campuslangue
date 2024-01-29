package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface EtudiantService extends GenericService<Etudiant, EtudiantRequestDTO, EtudiantDTO, LiteEtudiantDTO, ImportEtudiantRequestDTO> {

    boolean equalsByDto(EtudiantRequestDTO dto, Long id);
    boolean existByEmail(String email);

    List<EtudiantBranchDTO> findAll();

    boolean existByTelephone(String telephone);
    boolean findByTelephoneAndEmail(String telephone, String email);
    Etudiant findByMatricule(String matricule);

    List<EtudiantBranchDTO> getAllBySession(Long sessionId, Long salleId, Long niveauId);

    List<EtudiantBranchDTO> getAllWithUnpaid();
}
