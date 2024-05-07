package com.example.pr7.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Team")
public class Team {
    @Id
    private UUID teamId;
    private String name;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch=FetchType.EAGER)
    private List<User> users;
}

