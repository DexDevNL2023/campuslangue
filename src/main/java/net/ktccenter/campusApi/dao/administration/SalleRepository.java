package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.administration.Salle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalleRepository extends PagingAndSortingRepository<Salle, Long> {
    @Query("SELECT DISTINCT e FROM Salle e WHERE e.code = :code")
    Optional<Salle> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Salle e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<Salle> findByCodeAndLibelle(String code, String libelle);

    @Query("SELECT DISTINCT e FROM Salle e WHERE e.campus = :campus")
    List<Salle> findAllByCampus(Campus campus);

    @Query("SELECT DISTINCT e FROM Salle e WHERE e.campus.branche = :branche")
    List<Salle> findAllByBranche(Branche branche);
}
