package br.com.narigaz.restwithspringbootandjava.services;

import br.com.narigaz.restwithspringbootandjava.data.vo.v1.PersonVO;
import br.com.narigaz.restwithspringbootandjava.data.vo.v2.PersonVOV2;
import br.com.narigaz.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import br.com.narigaz.restwithspringbootandjava.mapper.DozerMapper;
import br.com.narigaz.restwithspringbootandjava.mapper.custom.PersonMapper;
import br.com.narigaz.restwithspringbootandjava.model.Person;
import br.com.narigaz.restwithspringbootandjava.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    PersonRepository repository;

    PersonMapper personMapper;

    @Autowired
    public PersonServices(PersonRepository repository, PersonMapper personMapper) {
        this.repository = repository;
        this.personMapper = personMapper;
    }

    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    public List<PersonVO> findAll() {

        logger.info("Finding All Persons");

        return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
    }
    public PersonVO findById(Long id) {

        logger.info("Finding one Person");

        var entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("No records found for this ID!"));
        return DozerMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO person) {

        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, Person.class);
        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public PersonVO update(PersonVO person) {

        logger.info("Updating one person!");

        var entity = repository.findById(person.getId()).orElseThrow( () -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return DozerMapper.parseObject(repository.save(entity), PersonVO.class);
    }
    public ResponseEntity<?> delete(Long id) {

        logger.info("Deleting one person!");

        Person entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);

        return ResponseEntity.noContent().build();
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating one person with V2!");

        var entity = personMapper.convertVoToEntity(person);
        return personMapper.convertEntityToVo(repository.save(entity));
    }
}
