package project.restapi.service;

import project.restapi.domain.entities.Role;
import project.restapi.domain.models.api.request.RoleChangeRequest;
import project.restapi.domain.models.api.response.RoleResponse;
import project.restapi.domain.models.api.response.UserAllResponse;
import project.restapi.repository.RoleRepository;

import java.util.List;
import java.util.Set;

public interface RoleService {
    void seedRoles();

    Set<Role> getRolesForAdmin();

    List<RoleResponse> getAll();}
