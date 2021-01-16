package com.asistlab.imagemaker.security;

import com.asistlab.imagemaker.model.Status;
import com.asistlab.imagemaker.model.User;
import com.asistlab.imagemaker.repository.UserRepository;
import com.asistlab.imagemaker.service.RolePermissionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
                rolePermissionService.getAuthorities(user.getRole())
        );
    }
}
