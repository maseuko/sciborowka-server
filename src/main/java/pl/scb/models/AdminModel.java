package pl.scb.models;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestAttribute;
import pl.scb.services.AuthDetails;

import javax.persistence.*;
import java.util.ArrayList;


@Entity
@NoArgsConstructor
public class AdminModel implements UserDetailsService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String login;
    @Column
    private String password;

    public AdminModel(@RequestAttribute("id") long id, @RequestAttribute("login") String login, @RequestAttribute("password") String password) {
        this.id = id;
        this.login = login;

        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(this.login, this.password, new ArrayList<>());
    }
}
