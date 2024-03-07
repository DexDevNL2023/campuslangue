package net.ktccenter.campusApi.entities.administration;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "parametres_institutions")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class ParametreInstitution extends BaseAuditingEntity {

    @Transient
    private List<JourOuvrable> jourOuvrables;
}
