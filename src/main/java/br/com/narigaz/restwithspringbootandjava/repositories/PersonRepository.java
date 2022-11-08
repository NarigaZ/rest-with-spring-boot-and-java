package br.com.narigaz.restwithspringbootandjava.repositories;

import br.com.narigaz.restwithspringbootandjava.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
