package com.detection.maize.disease.role;

import com.detection.maize.disease.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole roleCustomer);
}
