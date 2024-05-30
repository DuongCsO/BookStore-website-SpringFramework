package com.example.doAnTotNghiep.DoAnTotNghiep.repository;
import com.example.doAnTotNghiep.DoAnTotNghiep.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
