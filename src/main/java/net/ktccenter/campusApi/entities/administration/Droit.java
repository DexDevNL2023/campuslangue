package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "droits_utilisateur")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Droit extends BaseAuditingEntity {

	@Column(nullable = false)
	private String libelle;

    @Column(nullable = false, unique = true)
	private String key;

    @Column(nullable = false, unique = true)
	private String verbe;

	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Module module;
}
