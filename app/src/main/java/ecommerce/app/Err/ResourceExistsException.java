package ecommerce.app.Err;

public class ResourceExistsException extends RuntimeException{

    String source;
    String fieldValue;
    public ResourceExistsException() {

    }

    public ResourceExistsException(String source, String fieldValue) {
        super(String.format("%s with the name '%s' already exists",source,fieldValue));
        this.source = source;
        this.fieldValue = fieldValue;
    }
}
