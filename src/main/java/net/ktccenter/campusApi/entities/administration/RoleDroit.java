package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "permissions_utilisateur")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class RoleDroit extends BaseAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "droit_id")
    private Droit droit;

    private Boolean hasDroit = false;

    public RoleDroit(Droit droit, Boolean hasDroit) {
        this.droit = droit;
        this.hasDroit = hasDroit;
    }
    public RoleDroit(Role role, Boolean hasDroit) {
        this.role = role;
        this.hasDroit = hasDroit;
    }

    public RoleDroit(Role role, Droit droit, Boolean hasDroit) {
        this.role = role;
        this.droit = droit;
        this.hasDroit = hasDroit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDroit)) return false;
        RoleDroit that = (RoleDroit) o;
        return Objects.equals(role.getLibelle(), that.role.getLibelle()) &&
                Objects.equals(droit.getLibelle(), that.droit.getLibelle()) &&
                Objects.equals(hasDroit, that.hasDroit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, droit.getLibelle(), droit.getKey(), hasDroit);
    }

}
