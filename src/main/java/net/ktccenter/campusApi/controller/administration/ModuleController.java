package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ModuleController {
    ModuleDTO save(@RequestBody ModuleRequestDTO module);

    List<LiteModuleDTO> saveAll(@RequestBody List<ImportModuleRequestDTO> dtos);

    ModuleDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteModuleDTO> list();

    Page<LiteModuleDTO> pageQuery(Pageable pageable);

    void update(@RequestBody ModuleRequestDTO dto, @PathVariable("id") Long id);
}
