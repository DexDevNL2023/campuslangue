package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportNiveauRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.NiveauDTO;
import net.ktccenter.campusApi.dto.request.scolarite.NiveauRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface NiveauController {
    NiveauDTO save(@RequestBody NiveauRequestDTO dto);

    List<LiteNiveauDTO> saveAll(@RequestBody List<ImportNiveauRequestDTO> dtos);

    NiveauDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteNiveauDTO> list();

    Page<LiteNiveauDTO> pageQuery(Pageable pageable);

    void update(@RequestBody NiveauRequestDTO dto, @PathVariable("id") Long id);
}
