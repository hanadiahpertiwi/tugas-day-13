package com.ecommerce.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ecommerce.domain.AppUser;
import com.ecommerce.domain.Role;
import com.ecommerce.repo.AppUserRepository;
import com.ecommerce.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import com.security.spring.domain.AppUser;
//import com.security.spring.domain.Role;
//import com.security.spring.repo.AppUserRepository;
//import com.security.spring.repo.RoleRepository;

import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackOn = { SQLException.class, Exception.class })
@Slf4j
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SneakyThrows(UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String phone) {

        val appUser = appUserRepository.findByPhone(phone);

        if (Optional.ofNullable(appUser).isPresent()) {

            log.info("user {} is available in the database", phone);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            appUser.getRoles().forEach(role -> {

                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });

            return new User(appUser.getPhone(), appUser.getPassword(),
                    authorities);

        } else {

            log.info("user {} is not available in the database", phone);
            throw new UsernameNotFoundException("user " + phone + " is not available in the database");
        }
    }

    @SneakyThrows(Exception.class)
    public AppUser saveUser(AppUser appUser) {

        log.info("saving new user {} to the database", appUser.getName());

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        return appUserRepository.save(appUser);
    }

    @SneakyThrows(Exception.class)
    public Role saveRole(Role role) {

        log.info("saving new role {} to the database", role.getName());

        return roleRepository.save(role);
    }

    @SneakyThrows(Exception.class)
    public void addRoleToUser(String phone, String roleName) {

        log.info("adding role {} to user {}", roleName, phone);

        val appUser = appUserRepository.findByPhone(phone);
        val role = roleRepository.findByName(roleName);

        appUser.getRoles().add(role);
    }

    @SneakyThrows(Exception.class)
    public AppUser getUser(String phone) {

        log.info("getting user{} from the database", phone);

        return appUserRepository.findByPhone(phone);
    }

    @SneakyThrows(Exception.class)
    public List<AppUser> getUsers() {

        log.info("getting all users from the database");

        return appUserRepository.findAll();
    }

}

