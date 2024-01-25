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
    public NiveauDTO save(@RequestBody NiveauRequestDTO dto);

    public List<LiteNiveauDTO> saveAll(@RequestBody List<ImportNiveauRequestDTO> dtos);

    public NiveauDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteNiveauDTO> list();

    public Page<LiteNiveauDTO> pageQuery(Pageable pageable);

    public NiveauDTO update(@RequestBody NiveauRequestDTO dto, @PathVariable("id") Long id);
}
