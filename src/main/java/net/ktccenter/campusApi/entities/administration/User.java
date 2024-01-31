package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.enums.TypeUser;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "utilisateurs")
@EqualsAndHashCode(callSuper = false)
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Column(name = "id")
    private Long id;

    //info personnelles
    @Column(nullable = false)
    private String nom;

    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String uniqueId;

    @Size(min = 2, max = 10)
    @Column(length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(length = 256)
    private String imageUrl;

    private String password;
    private Boolean isVerified = false;
    private String verificationCode;
    private String passwordResetCode;

    @Column(nullable = false)
    private Boolean accountNonExpired;

    @Column(nullable = false)
    private Boolean accountNonLocked;

    @Column(nullable = false)
    private Boolean credentialsNonExpired;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    @JoinTable(  name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private TypeUser typeUser = TypeUser.INTERNE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Branche branche;

    private Boolean isGrant;

  public void setAuthorities(Role authority) {
    this.roles.add(authority);
  }

    //userDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return   this.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getLibelle()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
