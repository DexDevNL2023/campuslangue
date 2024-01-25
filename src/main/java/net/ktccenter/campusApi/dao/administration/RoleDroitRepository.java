package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDroitRepository extends PagingAndSortingRepository<RoleDroit, Long> {
    RoleDroit findByRoleAndDroit(Role role, Droit d);

    List<RoleDroit> findAllByDroit(Droit droit);
}
