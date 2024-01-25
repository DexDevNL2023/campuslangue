package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleFormationRepository extends PagingAndSortingRepository<ModuleFormation, Long> {
    @Query("SELECT DISTINCT e FROM ModuleFormation e WHERE e.code = :code")
    Optional<ModuleFormation> findByCode(String code);

    @Query("SELECT DISTINCT e FROM ModuleFormation e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<ModuleFormation> findByCodeAndLibelle(String code, String libelle);

    @Query("SELECT DISTINCT e FROM ModuleFormation e WHERE e.niveau = :niveau")
    List<ModuleFormation> findAllByNiveau(Niveau niveau);

    @Query("SELECT DISTINCT e FROM ModuleFormation e WHERE e.niveau.id = :niveauId")
    List<ModuleFormation> findAllByNiveauId(Long niveauId);
}
