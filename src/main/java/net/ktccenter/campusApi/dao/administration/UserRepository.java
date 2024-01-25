package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findByEnabled(Boolean enabled);

    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email")
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    User findByVerificationCode(String code);

    @Query("SELECT u FROM User u WHERE u.passwordResetCode = ?1")
    User findByPasswordResetCode(String code);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);

    User getById(User createdBy);

    @Query("SELECT DISTINCT e FROM User e WHERE e.nom = :nom OR e.email = :email")
    Optional<User> findByNomAndEmail(String nom, String email);

    @Query("SELECT DISTINCT e FROM User e WHERE e.nom = :nomOremail OR e.email = :nomOremail")
    User findByNomOrEmail(String nomOremail);
}
