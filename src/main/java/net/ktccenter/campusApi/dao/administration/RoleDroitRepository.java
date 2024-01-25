package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDroitRepository extends PagingAndSortingRepository<RoleDroit, Long> {
    @Query("SELECT DISTINCT e FROM RoleDroit e WHERE e.role = :role AND e.droit = :droit")
    RoleDroit findByRoleAndDroit(Role role, Droit droit);

    @Query("SELECT DISTINCT e FROM RoleDroit e WHERE e.droit = :droit")
    List<RoleDroit> findAllByDroit(Droit droit);

    @Query("SELECT DISTINCT e FROM RoleDroit e WHERE e.role = :role")
    List<RoleDroit> findAllByRole(Role role);
}
