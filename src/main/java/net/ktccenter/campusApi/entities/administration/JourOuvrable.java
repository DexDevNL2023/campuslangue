package net.ktccenter.campusApi.entities.administration;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.enums.Jour;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "jours_ouvrables")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class JourOuvrable extends BaseAuditingEntity {
    @Enumerated(EnumType.STRING)
    private Jour jour;
    private double[] intervalle;

    // Constructeur par défaut
    public JourOuvrable(Jour jour) {
        this.jour = jour;
        // Initialise l'intervalle avec des valeurs par défaut
        this.intervalle = new double[]{7.0, 17.0}; // Par exemple, 7h00 à 17h00
    }
}
