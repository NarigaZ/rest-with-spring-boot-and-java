package br.com.narigaz.restwithspringbootandjava.services;

import br.com.narigaz.restwithspringbootandjava.controllers.PersonController;
import br.com.narigaz.restwithspringbootandjava.data.vo.v1.PersonVO;
import br.com.narigaz.restwithspringbootandjava.exceptions.RequireObjectIsNullException;
import br.com.narigaz.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import br.com.narigaz.restwithspringbootandjava.mapper.DozerMapper;
import br.com.narigaz.restwithspringbootandjava.model.Person;
import br.com.narigaz.restwithspringbootandjava.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    public static final String NO_RECORDS_FOUND_FOR_THIS_ID = "No records found for this ID!";
    PersonRepository repository;

    @Autowired
    public PersonServices(PersonRepository repository) {
        this.repository = repository;
    }

    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<PersonVO> findAll() {

        logger.info("Finding All Persons");
        var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
        persons.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return persons;
    }
    public PersonVO findById(Long id) {

        logger.info("Finding one Person");

        var entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException(NO_RECORDS_FOUND_FOR_THIS_ID));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {

        if (person == null) throw new RequireObjectIsNullException();

        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {

        if (person == null) throw new RequireObjectIsNullException();

        logger.info("Updating one person!");

        var entity = repository.findById(person.getKey()).orElseThrow( () -> new ResourceNotFoundException(NO_RECORDS_FOUND_FOR_THIS_ID));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) {

        logger.info("Disabling one Person");

        repository.disablePerson(id);
        var entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException(NO_RECORDS_FOUND_FOR_THIS_ID));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }
    public ResponseEntity<?> delete(Long id) {

        logger.info("Deleting one person!");

        Person entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException(NO_RECORDS_FOUND_FOR_THIS_ID));
        repository.delete(entity);

        return ResponseEntity.noContent().build();
    }
}
