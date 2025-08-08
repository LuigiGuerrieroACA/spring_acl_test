package com.example.spring_acl_test.infra;

import com.example.spring_acl_test.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;  // your JPA repo or DAO

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new MyUserDetails(user);
    }
}

