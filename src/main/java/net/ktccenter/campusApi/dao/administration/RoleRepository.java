package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
  @Query("SELECT DISTINCT r FROM Role r WHERE r.libelle = :roleName")
  Role findByRoleName(String roleName);

    Optional<Role> findByLibelle(String libelle);
}
