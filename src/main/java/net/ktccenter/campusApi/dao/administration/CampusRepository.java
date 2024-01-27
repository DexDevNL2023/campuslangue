package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampusRepository extends PagingAndSortingRepository<Campus, Long> {
    @Query("SELECT DISTINCT e FROM Campus e WHERE e.code = :code")
    Optional<Campus> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Campus e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<Campus> findByCodeAndLibelle(String code, String libelle);

    @Query("SELECT DISTINCT e FROM Campus e WHERE e.branche = :branche")
    List<Campus> findAllByBranche(Branche branche);

    @Query("SELECT DISTINCT e FROM Campus e WHERE e.branche.id = :brancheId")
    List<Campus> findAllByBrancheId(Long brancheId);
}
