package com.kaluzny.demo.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(name = "Book", description = "Book is ...", oneOf = Book.class)
public class Book {

    @Schema(description = "Unique identifier of the Book.", example = "1", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Schema(description = "Name of the Book.", example = "Volvo", required = true)
    @Size(max = 50)
    private String name;

    @Schema(description = "Color of the Book.", example = "Red", required = true)
    @Size(max = 50)
    private String type;

    @Schema(description = "Content of the Book.", example = "Red", required = true)
    @Size(max = 200)
    private String content;
}
