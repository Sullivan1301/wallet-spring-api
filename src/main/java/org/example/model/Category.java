package org.example.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Category {
    private int categoryId;
    private String categoryName;
    private String categoryType;

    public Category( String categoryName, String categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}
