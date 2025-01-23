package com.api.book.bootrestbook.services;

// import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.book.bootrestbook.dao.BookRepository;
import com.api.book.bootrestbook.entities.Book;

@Component
public class BookService {

    // private static List<Book> list = new ArrayList<>();

    // static{
    //     list.add(new Book(1, "Java Edition 5", "ABC"));
    //     list.add(new Book(2, "Cpp Edition 3", "XYZ"));
    //     list.add(new Book(3, "Python Edition 2", "UVZ"));
    // }

    @Autowired
    private BookRepository bookRepository;

    //Get all books
    public List<Book> getAllBooks(){
        List<Book> list = (List<Book>)this.bookRepository.findAll();
        return list;
    }

    //Get single book by id
    public Book getBookById(int id){

        Book book = null;
        try {
            // book = list.stream().filter(e->e.getId()==id).findAny().get();
            book = this.bookRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;

    }

    //Adding the book
    public Book addBook(Book b){

        // list.add(b);
        Book result = bookRepository.save(b);
        return result;

    }

    //Delete book
    public void deleteBook(int bid){
        //Basically this functions ony takes the books which we don't want to delete, and it simply left the id which we want to delete

        //filter() is a predicate which decides true or false and also it is used to intercept the request

        // list = list.stream().filter(book->book.getId()!=id).collect(Collectors.toList());

        // list = list.stream().filter(book->{
        //     if(book.getId() != bid){
        //         return true;
        //     }else{
        //         return false;
        //     }
        // }).collect(Collectors.toList());

        bookRepository.deleteById(bid);

    }

    //Update the book
    public void updateBook(Book book, int bookId){

        //map() function takes one mapper function to apply on each stream element
        // list = list.stream().map(b->{
        //     if(b.getId() == bookId){
        //         b.setTitle(book.getTitle());
        //         b.setAuthor(book.getAuthor());
        //     }
        //     return b;
        // }).collect(Collectors.toList());

        book.setId(bookId);
        bookRepository.save(book);

    }
    
}
