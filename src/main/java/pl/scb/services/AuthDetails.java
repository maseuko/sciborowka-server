package pl.scb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.scb.models.AdminModel;
import pl.scb.models.AdminRoles;
import pl.scb.models.Role;
import pl.scb.records.Authority;
import pl.scb.repo.AdminRolesRepo;
import pl.scb.repo.RoleRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuthDetails {
    @Autowired
    private AdminRolesRepo adminRolesRepo;
    @Autowired
    private RoleRepo roleRepo;
    public List<Authority> getAuthorities(AdminModel adminModel){
        List<Authority> authorities = new ArrayList<>();
        Optional<List<AdminRoles>> rolesIds = this.adminRolesRepo.findByAdminId(adminModel.getId());
        if(rolesIds.isPresent()){
            for(AdminRoles userRole: rolesIds.get()){
                Optional<Role> role = this.roleRepo.findById(userRole.getRoleId());
                role.ifPresent(value -> authorities.add(new Authority(value.getName())));
            }
        }

        return authorities;
    }
}
