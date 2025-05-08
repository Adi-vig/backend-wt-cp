package com.sudoku.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    private String email;
    private String name;
    private String password;
    private int userlevel;
}
