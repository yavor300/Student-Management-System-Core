package project.restapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.restapi.domain.entities.Role;
import project.restapi.repository.RoleRepository;
import project.restapi.service.RoleService;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seedRoles() {
        if (roleRepository.count() == 0){
            roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            roleRepository.saveAndFlush(new Role("ROLE_TEACHER"));
            roleRepository.saveAndFlush(new Role("ROLE_STUDENT"));
        }
    }

    @Override
    public Set<Role> getRolesForAdmin() {
        return new HashSet<>(roleRepository.findAll());
    }
}