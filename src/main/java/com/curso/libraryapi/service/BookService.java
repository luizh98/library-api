package com.curso.libraryapi.service;

import com.curso.libraryapi.model.entity.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
  Book save(Book any);

  Optional<Book> getById(Long id);

  void delete(Book book);

  Book update(Book book);

  Page<Book> find(Book filter, Pageable pageRequest);

  Optional<Book> getBookByIsbn(String isbn);
}
