package br.com.narigaz.restwithspringbootandjava.repositories;

import br.com.narigaz.restwithspringbootandjava.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
