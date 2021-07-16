package project.restapi.service;

import project.restapi.domain.entities.Role;

import java.util.Set;

public interface RoleService {
    void seedRoles();

    Set<Role> getRolesForAdmin();
}
