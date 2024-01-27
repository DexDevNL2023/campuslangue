package net.ktccenter.campusApi.controller.finance;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportRubriqueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RubriqueController {
	RubriqueDTO save(@RequestBody RubriqueRequestDTO dto);

	List<LiteRubriqueDTO> saveAll(@RequestBody List<ImportRubriqueRequestDTO> dtos);

	RubriqueDTO findById(@PathVariable("id") Long id);

	void delete(@PathVariable("id") Long id);

	List<LiteRubriqueDTO> list();

	Page<LiteRubriqueDTO> pageQuery(Pageable pageable);

	void update(@RequestBody RubriqueRequestDTO dto, @PathVariable("id") Long id);
}
