package project.restapi.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import project.restapi.domain.entities.Role;
import project.restapi.repository.RoleRepository;
import project.restapi.service.RoleService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class RoleServiceTests {
    private static final Long ZERO_COUNT = 0L;
    public static final int ADMIN_ROLES_COLLECTION_SIZE = 3;

    private RoleRepository mockRoleRepository;
    private RoleService roleService;

    @Before
    public void init() {
        mockRoleRepository = Mockito.mock(RoleRepository.class);
        roleService = new RoleServiceImpl(mockRoleRepository, new ModelMapper());
    }

    @Test
    public void seedRoles_Should_Seed_Correctly() {
        Mockito.when(mockRoleRepository.count())
                .thenReturn(ZERO_COUNT);

        Mockito.when(mockRoleRepository.saveAndFlush(any(Role.class)))
                .thenReturn(new Role());

        roleService.seedRoles();

        Mockito.verify(mockRoleRepository, times(3)).saveAndFlush(any(Role.class));
    }

    @Test
    public void getRolesForAdmin_Should_Return_All_Roles_For_Admin() {
        Mockito.when(mockRoleRepository.findAll())
                .thenReturn(List.of(new Role(), new Role(), new Role()));

        Set<Role> result = roleService.getRolesForAdmin();

        Assert.assertEquals(ADMIN_ROLES_COLLECTION_SIZE, result.size());
    }
}
