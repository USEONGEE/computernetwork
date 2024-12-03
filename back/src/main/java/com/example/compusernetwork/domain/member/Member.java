package com.example.compusernetwork.domain.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    private String name;
    private String password;
}
