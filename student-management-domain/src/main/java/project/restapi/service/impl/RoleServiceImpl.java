package project.restapi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.restapi.domain.entities.Role;
import project.restapi.domain.models.api.request.RoleChangeRequest;
import project.restapi.domain.models.api.response.RoleResponse;
import project.restapi.domain.models.api.response.UserAllResponse;
import project.restapi.repository.RoleRepository;
import project.restapi.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
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

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .collect(Collectors.toList());
    }
}