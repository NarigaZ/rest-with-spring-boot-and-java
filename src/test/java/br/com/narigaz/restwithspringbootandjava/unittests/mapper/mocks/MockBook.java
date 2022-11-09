package br.com.narigaz.restwithspringbootandjava.unittests.mapper.mocks;

import br.com.narigaz.restwithspringbootandjava.data.vo.v1.BookVO;
import br.com.narigaz.restwithspringbootandjava.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(dateMock(number));
        book.setPrice(number.doubleValue());
        book.setId(number);
        book.setTitle("Title Test" + number);
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setAuthor("Author Test" + number);
        book.setLaunchDate(dateMock(number));
        book.setPrice(number.doubleValue());
        book.setKey(number);
        book.setTitle("Title Test" + number);
        return book;
    }

    public Date dateMock(Integer number) {
        return new Date(number);
    }

}
