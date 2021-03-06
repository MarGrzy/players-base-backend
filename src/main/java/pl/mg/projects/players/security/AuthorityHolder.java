package pl.mg.projects.players.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public interface AuthorityHolder {
    Set<SimpleGrantedAuthority> addUserRole();
    Set<SimpleGrantedAuthority> addAdminRole();
}
