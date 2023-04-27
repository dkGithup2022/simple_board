package com.dk0124.respos.member;


import com.dk0124.respos.common.BaseEntity;
import com.dk0124.respos.role.Role;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "nickname")
})
public class Member extends BaseEntity {

    @Id @Getter
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "member_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;


    @Getter @Setter
    @NotBlank    @Size(max=255)
    private String email;

    @Getter @Setter
    @NotBlank    @Size(max=255)
    private String nickName;

    @Getter @Setter
    @Column(length = 1000)
    @NotBlank
    private String password;


    @Getter  @Setter
    @ManyToMany
    @JoinTable(name = "member_role", joinColumns = @JoinColumn(name = "role"), inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<Role> roles = new HashSet<>();


    public Member(String email, String nickName, String password, Set<Role> roles) {
        this(null, email, nickName, password, null);
        if (roles != null) {
            this.roles = new HashSet<>();
            roles.forEach(e -> this.roles.add(e));
        }
    }
}
