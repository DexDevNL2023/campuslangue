package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DiplomeController {
    DiplomeDTO save(@RequestBody DiplomeRequestDTO dto);

    List<LiteDiplomeDTO> saveAll(@RequestBody List<ImportDiplomeRequestDTO> dtos);

    DiplomeDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteDiplomeDTO> list();

    Page<LiteDiplomeDTO> pageQuery(Pageable pageable);

    DiplomeDTO update(@RequestBody DiplomeRequestDTO dto, @PathVariable("id") Long id);
}
