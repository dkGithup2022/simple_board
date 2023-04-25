package com.dk0124.respos.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "role")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(name = "role", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private ERole role;
}
