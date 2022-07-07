package com.sudeepcv.springboot.blog.security;

import com.sudeepcv.springboot.blog.entity.Role;
import com.sudeepcv.springboot.blog.entity.User;
import com.sudeepcv.springboot.blog.repository.UserRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
       User user= userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail).orElseThrow(()->{
             return new UsernameNotFoundException(" user not found with user name or email"+usernameOrEmail );
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
       return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
