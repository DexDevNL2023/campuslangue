package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.branch.FormateurBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface FormateurService extends GenericService<Formateur, FormateurRequestDTO, FormateurDTO, LiteFormateurDTO, ImportFormateurRequestDTO> {

  boolean equalsByDto(FormateurRequestDTO dto, Long id);
  boolean existByEmail(String email);

    List<FormateurBranchDTO> findAll();

  boolean existByTelephone(String telephone);
  boolean findByTelephoneAndEmail(String telephone, String email);
  Formateur findByMatricule(String matricule);
}
