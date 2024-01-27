package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportRoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleDroitRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RoleDroitController {
    RoleDroitDTO save(@RequestBody RoleDroitRequestDTO roleDroit);

    List<LiteRoleDroitDTO> saveAll(@RequestBody List<ImportRoleDroitRequestDTO> dtos);

    RoleDroitDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteRoleDroitDTO> list();

    Page<LiteRoleDroitDTO> pageQuery(Pageable pageable);

    void update(@RequestBody RoleDroitRequestDTO dto, @PathVariable("id") Long id);
}
