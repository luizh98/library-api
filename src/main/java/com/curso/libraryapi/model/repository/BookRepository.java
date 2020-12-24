package com.curso.libraryapi.model.repository;

import com.curso.libraryapi.model.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
  boolean existsByIsbn(String isbn);

  Optional<Book> findByIsbn(String isbn);
}
