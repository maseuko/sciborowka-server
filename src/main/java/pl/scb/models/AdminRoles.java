package pl.scb.models;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class AdminRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminRoleId;
    @Column
    private long adminId;
    @Column
    private long roleId;

    public long getAdminRoleId() {
        return adminRoleId;
    }

    public long getAdminId() {
        return adminId;
    }

    public long getRoleId() {
        return roleId;
    }
}
