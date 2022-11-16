package br.com.narigaz.restwithspringbootandjava.services;

import br.com.narigaz.restwithspringbootandjava.controllers.BookController;
import br.com.narigaz.restwithspringbootandjava.data.vo.v1.BookVO;
import br.com.narigaz.restwithspringbootandjava.exceptions.RequireObjectIsNullException;
import br.com.narigaz.restwithspringbootandjava.exceptions.ResourceNotFoundException;
import br.com.narigaz.restwithspringbootandjava.mapper.DozerMapper;
import br.com.narigaz.restwithspringbootandjava.model.Book;
import br.com.narigaz.restwithspringbootandjava.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {
    BookRepository repository;

    @Autowired
    public BookServices(BookRepository repository) {
        this.repository = repository;
    }

    private final Logger logger = Logger.getLogger(BookServices.class.getName());

    public List<BookVO> findAll() {
        logger.info("Finding All Books");
        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books.forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
        return books;
    }
    public BookVO findById(Integer id) {
        logger.info("Finding one Book");
        var entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {
        if (book == null) throw new RequireObjectIsNullException();
        logger.info("Creating one book!");
        var entity = DozerMapper.parseObject(book, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if (book == null) throw new RequireObjectIsNullException();
        logger.info("Updating one book!");
        var entity = repository.findById(book.getKey()).orElseThrow( () -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
    public ResponseEntity<?> delete(Integer id) {
        logger.info("Deleting one book!");
        Book entity = repository.findById(id).orElseThrow( () -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);

        return ResponseEntity.noContent().build();
    }
}
