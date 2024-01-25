package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Branche;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrancheRepository extends PagingAndSortingRepository<Branche, Long> {
    @Query("SELECT DISTINCT e FROM Branche e WHERE e.code = :code")
    Optional<Branche> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Branche e WHERE e.code = :code OR e.ville = :ville")
    Optional<Branche> findByCodeAndVille(String code, String ville);
}
