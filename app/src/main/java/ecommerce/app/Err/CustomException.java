package ecommerce.app.err;

public class CustomException extends RuntimeException {

    public CustomException(String resourceName, String message) {
        super(resourceName+" "+message);
    }
}
