package br.com.narigaz.restwithspringbootandjava.unittests.mockito.services;

import br.com.narigaz.restwithspringbootandjava.data.vo.v1.BookVO;
import br.com.narigaz.restwithspringbootandjava.exceptions.RequireObjectIsNullException;
import br.com.narigaz.restwithspringbootandjava.model.Book;
import br.com.narigaz.restwithspringbootandjava.repositories.BookRepository;
import br.com.narigaz.restwithspringbootandjava.services.BookServices;
import br.com.narigaz.restwithspringbootandjava.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

    MockBook input;

    @InjectMocks
    private BookServices service;

    @Mock
    private BookRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        var book = service.findAll();

        assertNotNull(book);
        assertEquals(14, book.size());

        var bookOne = book.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getLinks());

        assertTrue(bookOne.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
        assertEquals("Author Test1", bookOne.getAuthor());
        assertEquals(dateMock(1), bookOne.getLaunchDate());
        assertEquals(1.0, bookOne.getPrice());
        assertEquals("Title Test1", bookOne.getTitle());

        var bookFour = book.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getLinks());

        assertTrue(bookFour.toString().contains("links: [</api/v1/book/4>;rel=\"self\"]"));
        assertEquals("Author Test4", bookFour.getAuthor());
        assertEquals(dateMock(4), bookFour.getLaunchDate());
        assertEquals(4.0, bookFour.getPrice());
        assertEquals("Title Test4", bookFour.getTitle());

        var bookSeven = book.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getLinks());

        assertTrue(bookSeven.toString().contains("links: [</api/v1/book/7>;rel=\"self\"]"));
        assertEquals("Author Test7", bookSeven.getAuthor());
        assertEquals(dateMock(7), bookSeven.getLaunchDate());
        assertEquals(7.0, bookSeven.getPrice());
        assertEquals("Title Test7", bookSeven.getTitle());
    }

    @Test
    void findById() {
        Book entity = input.mockEntity(1);

        when(repository.findById(1)).thenReturn(Optional.of(entity));

        var result = service.findById(1);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(dateMock(1), result.getLaunchDate());
        assertEquals(1.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void create() {
        Book entity = input.mockEntity();
        Book persisted = input.mockEntity(1);
        BookVO vo = input.mockVO(0);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);

        assertNotNull(result);
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/v1/book/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertEquals(dateMock(1), result.getLaunchDate());
        assertEquals(1.0, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    @Test
    void createWithNullPerson() {
        Exception exception = assertThrows(RequireObjectIsNullException.class, () -> service.create(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        BookVO vo = input.mockVO(5);
        vo.setPrice(20.0);
        Book entity = input.mockEntity(5);
        Book persisted = input.mockEntity(5);
        persisted.setPrice(20.0);

        when(repository.findById(5)).thenReturn(Optional.ofNullable(entity));
        assert entity != null;
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/v1/book/5>;rel=\"self\"]"));
        assertEquals("Author Test5", result.getAuthor());
        assertEquals(dateMock(5), result.getLaunchDate());
        assertEquals(20.0, result.getPrice());
        assertEquals("Title Test5", result.getTitle());
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequireObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
    }

    public Date dateMock(Integer number) {
        return new Date(number);
    }
}