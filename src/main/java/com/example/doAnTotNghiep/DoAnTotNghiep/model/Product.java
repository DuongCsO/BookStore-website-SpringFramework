package com.example.doAnTotNghiep.DoAnTotNghiep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product")
public class Product extends BaseModel {
    @NotBlank(message = "This field is required!")
    private String name;
    @NotNull(message = "This field is required!")
    @Min(value = 1, message = "Must be a number > 0")
    private int pageNumber;
    @NotBlank(message = "This field is required!")
    @Column(length = 1000)
    private String description;
    private long cost;
    private int quantity;
    private int publishYear;
    @NotBlank(message = "This field is required!")
    private String language;
    @NotBlank(message = "This field is required!")
    private String form;
    private String image;
    @Column(columnDefinition = "json")
    private String preImg;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

}
