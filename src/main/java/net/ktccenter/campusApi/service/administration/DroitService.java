package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.DroitRequestDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface DroitService extends GenericService<Droit, DroitRequestDTO, DroitDTO, LiteDroitDTO, ImportDroitRequestDTO> {
    boolean existsByVerbeAndKeyAndLibelle(String verbe, String key, String libelle);

    boolean equalsByDto(DroitRequestDTO dto, Long id);

    Droit findByLibelle(String libelle);

    List<LiteDroitDTO> findAll();

}
