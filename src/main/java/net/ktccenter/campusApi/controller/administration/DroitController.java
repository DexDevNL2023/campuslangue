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
    public DroitDTO save(@RequestBody DroitRequestDTO droit);

    public List<LiteDroitDTO> saveAll(@RequestBody List<ImportDroitRequestDTO> dtos);

    public DroitDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteDroitDTO> list();

    public Page<LiteDroitDTO> pageQuery(Pageable pageable);

    public DroitDTO update(@RequestBody DroitRequestDTO dto, @PathVariable("id") Long id);
}
