package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EtudiantController {
    EtudiantDTO save(@RequestBody EtudiantRequestDTO dto);

    List<LiteEtudiantDTO> saveAll(@RequestBody List<ImportEtudiantRequestDTO> dtos);

    EtudiantDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteEtudiantDTO> list();

    List<LiteEtudiantDTO> getAllBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("salleId") Long salleId, @PathVariable("niveauId") Long niveauId);

    List<LiteEtudiantDTO> getAllWithUnpaid();

    Page<LiteEtudiantDTO> pageQuery(Pageable pageable);

    EtudiantDTO update(@RequestBody EtudiantRequestDTO dto, @PathVariable("id") Long id);
}
