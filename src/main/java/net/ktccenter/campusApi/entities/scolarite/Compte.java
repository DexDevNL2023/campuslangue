package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "comptes_paiement")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Compte extends BaseAuditingEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Transient
    private BigDecimal solde;

    @Transient
    private BigDecimal resteApayer;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Inscription inscription;
}
