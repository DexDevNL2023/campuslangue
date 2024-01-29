package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CompteBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Compte;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface CompteService extends GenericService<Compte, CompteRequestDTO, CompteDTO, LiteCompteDTO, ImportCompteRequestDTO> {

    boolean equalsByDto(CompteRequestDTO dto, Long id);

    Compte findByCode(String code);

    List<CompteBranchDTO> findAll();

    boolean existByCode(String code);
}
