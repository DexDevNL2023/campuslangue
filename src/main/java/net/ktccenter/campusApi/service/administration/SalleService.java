package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.OccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SalleBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.OccupationSalleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SalleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Salle;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface SalleService extends GenericService<Salle, SalleRequestDTO, SalleDTO, LiteSalleDTO, ImportSalleRequestDTO> {
    boolean existsByCodeAndLibelle(String code, String libelle);

    boolean equalsByDto(SalleRequestDTO dto, Long id);

    Salle findByCode(String code);

    OccupationSalleDTO changeOccupation(OccupationSalleRequestDTO dto);

    OccupationSalleDTO getOccupationById(Long occupationId);

    OccupationSalleDTO getOccupationByCode(String occupationCode);

    List<LiteOccupationSalleDTO> getPlageDisponible(Long salleId);

    List<LiteOccupationSalleDTO> getPlannigSalle(Long salleId);

    List<SalleBranchDTO> findAll();
}
