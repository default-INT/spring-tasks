package by.gstu.springsecurity.security;

import by.gstu.springsecurity.model.RolePermission;
import by.gstu.springsecurity.model.Status;
import by.gstu.springsecurity.model.User;
import by.gstu.springsecurity.repository.UserRepository;
import by.gstu.springsecurity.service.RolePermissionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RolePermissionService rolePermissionService;

    public UserDetailsServiceImpl(UserRepository userRepository, RolePermissionService rolePermissionService) {
        this.userRepository = userRepository;
        this.rolePermissionService = rolePermissionService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User" + username + " doesn't exist"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                rolePermissionService.getPermissions(user.getRole()).stream()
                        .map(RolePermission::getPermission)
                        .map(permission ->  new SimpleGrantedAuthority(permission.name()))
                        .collect(Collectors.toSet())
        );
    }
}
