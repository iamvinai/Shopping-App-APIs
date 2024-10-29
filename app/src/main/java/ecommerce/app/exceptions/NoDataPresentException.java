package ecommerce.app.exceptions;


public class NoDataPresentException extends RuntimeException {
    String resourceName;

    public NoDataPresentException() {
        
    }
    public NoDataPresentException(String resourceName) {
        super(String.format("There is no %s data",resourceName));
        this.resourceName = resourceName;
    }

}

