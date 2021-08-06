package com.adobe.bookstore.books.domains;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOKS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class Book implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    @NotBlank(message = BookValidationMessages.BOOK_ID_MANDATORY)
    @Size(min = 2, max = 36, message = BookValidationMessages.BOOK_ID_WRONG_SIZE)
    private String bookId;

    @Column(name = "NAME", nullable = false)
    @NotBlank(message = BookValidationMessages.BOOK_NAME_MANDATORY)
    @Size(min = 2, max = 36, message = BookValidationMessages.BOOK_NAME_WRONG_SIZE)
    private String bookName;

    @Column(name = "QUANTITY", nullable = false)
    @Min(value = 0, message = BookValidationMessages.BOOK_QUANTITY_NEGATIVE)
    private int quantity;
}
