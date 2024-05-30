package com.example.doAnTotNghiep.DoAnTotNghiep.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'USER'")
    private UserRole role;
    private String username;
    private String email;
    private String password;
    private String fullname;
    @Column(columnDefinition = "tinyint default 1")
    private byte status;
}
