package com.dk0124.respos;

import com.dk0124.respos.role.ERole;
import com.dk0124.respos.role.Role;
import com.dk0124.respos.role.dao.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsertEnums implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (ERole role : ERole.values())
            roleRepository.save(new Role(role));

    }
}
