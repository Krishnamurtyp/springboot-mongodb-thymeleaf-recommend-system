package com.bigdata.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Users")
public class User extends UpdateAuditable{
    @Id private String id;
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private Image profileImage;
    private Set<String> roles;
    private Set<String> authorities;
}
