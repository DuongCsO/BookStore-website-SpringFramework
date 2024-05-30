package com.example.doAnTotNghiep.DoAnTotNghiep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_author")
public class Author extends BaseModel{
    @NotBlank(message = "This field is required!")
    private String name;
    @NotBlank(message = "This field is required!")
    @Column(length = 1000)
    private String description;
}
