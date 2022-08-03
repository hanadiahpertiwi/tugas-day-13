package com.ecommerce.repo;

import com.ecommerce.domain.AppUser;
import com.ecommerce.security.SecurityConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByPhone(String phone);
}
