package com.curso.libraryapi.api.exception;

import com.curso.libraryapi.exception.BusinessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.validation.BindingResult;

public class ApiErros {
  private List<String> errors;

  public ApiErros(BindingResult bindingResult) {
    this.errors = new ArrayList<>();
    bindingResult
      .getAllErrors()
      .forEach(error -> this.errors.add(error.getDefaultMessage()));
  }

  public ApiErros(BusinessException ex) {
    this.errors = Arrays.asList(ex.getMessage());
  }

  public List<String> getErrors() {
    return errors;
  }
}
