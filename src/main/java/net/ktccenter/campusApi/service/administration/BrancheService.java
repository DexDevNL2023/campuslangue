package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportBrancheRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.reponse.administration.BrancheDTO;
import net.ktccenter.campusApi.dto.request.administration.BrancheRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.service.GenericService;

public interface BrancheService extends GenericService<Branche, BrancheRequestDTO, BrancheDTO, LiteBrancheDTO, ImportBrancheRequestDTO> {
    boolean equalsByDto(BrancheRequestDTO dto, Long id);

    boolean existsByCodeAndVille(String code, String ville);

    Branche findByCode(String code);
}
