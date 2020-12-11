package com.kaluzny.demo.web;

import com.kaluzny.demo.domain.Book;
import com.kaluzny.demo.domain.BookRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Book", description = "the Book API")
public class BookRestController {

    private final BookRepository repository;

    @Operation(summary = "Add a new Book", description = "endpoint for creating an entity", tags = {"Book"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Book already exists")})
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Book saveBook(
            @Parameter(description = "Book", required = true) @NotNull @RequestBody Book book) {
        log.info("saveBook() - start: book = {}", book);
        Book savedBook = repository.save(book);
        log.info("saveBook() - end: savedBook = {}", savedBook.getId());
        return savedBook;
    }

    @Operation(summary = "Find all Books", description = " ", tags = {"Book"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class))))})
    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> getAllBooks() {
        log.info("getAllBooks() - start");
        Collection<Book> collection = repository.findAll();
        log.info("getAllBooks() - end");
        return collection;
    }

    @Operation(summary = "Find book by ID", description = "Returns a single book", tags = {"Book"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book not found")})
    @GetMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(
            @Parameter(description = "Id of the Book to be obtained. Cannot be empty.", required = true)
            @PathVariable Long id) {
        log.info("getBookById() - start: id = {}", id);
        Book receivedBook = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id = Not found"));
        log.info("getBookById() - end: Book = {}", receivedBook.getId());
        return receivedBook;
    }

    @Hidden
    @Operation(summary = "Find book by name", description = " ", tags = {"Book"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class))))})
    @GetMapping(value = "/books", params = {"name"})
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> findBookByName(
            @Parameter(description = "Name of the Book to be obtained. Cannot be empty.", required = true)
            @RequestParam(value = "name") String name) {
        log.info("findBookByName() - start: name = {}", name);
        Collection<Book> collection = repository.findByName(name);
        log.info("findBookByName() - end: collection = {}", collection);
        return collection;
    }

    @Operation(summary = "Update an existing Book", description = "need to fill", tags = {"Book"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "405", description = "Validation exception")})
    @PutMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book refreshBook(
            @Parameter(description = "Id of the Book to be update. Cannot be empty.", required = true)
            @PathVariable Long id,
            @Parameter(description = "Book to update.", required = true)
            @RequestBody Book book) {
        log.info("refreshBook() - start: id = {}, book = {}", id, book);
        Book updatedBook = repository.findById(id)
                .map(entity -> {
                    entity.setName(book.getName());
                    entity.setType(book.getType());
                    entity.setContent(book.getContent());
                    entity.setImages(book.getImages());
                    return repository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Book with id = Not found"));
        log.info("refreshBook() - end: updatedBook = {}", updatedBook);
        return updatedBook;
    }

    @Operation(summary = "Deletes a Book", description = "need to fill", tags = {"Book"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Book not found")})
    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookById(
            @Parameter(description = "Id of the Book to be delete. Cannot be empty.", required = true)
            @PathVariable Long id) {
        log.info("removeBookById() - start: id = {}", id);
        repository.deleteById(id);
        log.info("removeBookById() - end: id = {}", id);
    }
}
