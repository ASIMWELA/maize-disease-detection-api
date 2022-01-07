package com.detection.maize.disease.config;

import com.detection.maize.disease.enums.ERole;
import com.detection.maize.disease.role.Role;
import com.detection.maize.disease.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class DbInit {
    @Bean
    CommandLineRunner init(RoleRepository roleRepository){
        return args -> {
           // clean slate
//            roleRepository.deleteAll();
//
//            //reinitialize
//            Role roleUser = new Role();
//            roleUser.setName(ERole.ROLE_USER);
//            roleUser.setUuid("gjsklport42n");
//
//            Role roleAdmin = new Role();
//            roleAdmin.setName(ERole.ROLE_ADMIN);
//            roleAdmin.setUuid("gjsklportghn");
//            roleRepository.saveAll(Stream.of(roleAdmin, roleUser).collect(Collectors.toList()));

        };
    }
}
