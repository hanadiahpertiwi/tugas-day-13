package com.ecommerce.repo;

import com.ecommerce.domain.Role;
import com.ecommerce.security.SecurityConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByName(String name);
}
