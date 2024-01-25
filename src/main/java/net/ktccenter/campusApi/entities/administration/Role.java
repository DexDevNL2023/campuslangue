package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "roles_utilisateur")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Role extends BaseAuditingEntity {

    @Column(nullable = false, unique = true)
    private String libelle;

    private Boolean isSuper = false;

    private Boolean isGrant = false;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<RoleDroit> permissions = new ArrayList<>();
}
