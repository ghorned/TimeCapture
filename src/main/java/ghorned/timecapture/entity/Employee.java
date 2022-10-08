package ghorned.timecapture.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ghorned.timecapture.utility.AccessAuthority;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "employee")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank
    @Size(min = 8, max = 72)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Min(0) @Max(168)
    @Column(name = "week_hrs_min")
    private double weekHrsMin;

    @Min(0) @Max(168)
    @Column(name = "week_hrs_max")
    private double weekHrsMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_authority")
    private AccessAuthority accessAuthority;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "employee", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Punch> punches = new ArrayList<>();

    public List<Punch> getPunches() {
        punches.sort(Comparator.comparing(Punch::getTime));
        return punches;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.accessAuthority);
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
