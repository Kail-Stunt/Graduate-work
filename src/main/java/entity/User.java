
package entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String image;

    public User() {}

    public User(Integer id, String username, String password, String firstName, String lastName, String phone, UserRole role, String image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
    }

    public static User builder() {
        return new User();
    }

    public User withId(Integer id) { this.id = id; return this; }
    public User withUsername(String username) { this.username = username; return this; }
    public User withPassword(String password) { this.password = password; return this; }
    public User withFirstName(String firstName) { this.firstName = firstName; return this; }
    public User withLastName(String lastName) { this.lastName = lastName; return this; }
    public User withPhone(String phone) { this.phone = phone; return this; }
    public User withRole(UserRole role) { this.role = role; return this; }
    public User withImage(String image) { this.image = image; return this; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
