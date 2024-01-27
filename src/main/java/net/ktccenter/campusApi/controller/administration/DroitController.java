package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.DroitRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DroitController {
    DroitDTO save(@RequestBody DroitRequestDTO droit);

    List<LiteDroitDTO> saveAll(@RequestBody List<ImportDroitRequestDTO> dtos);

    DroitDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteDroitDTO> list();

    Page<LiteDroitDTO> pageQuery(Pageable pageable);

    void update(@RequestBody DroitRequestDTO dto, @PathVariable("id") Long id);
}
