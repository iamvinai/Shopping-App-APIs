package ecommerce.app.exceptions;

public class CategoryExistsException extends RuntimeException{

    String fieldValue;
    String fieldName;
    public CategoryExistsException() {
    }

    public CategoryExistsException(String fieldValue, String fieldName) {
        super(String.format("Category with the name '%s' already exists",fieldValue));
        this.fieldValue = fieldValue;
        this.fieldName = fieldName;
    }
}
