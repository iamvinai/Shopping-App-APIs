package ecommerce.app.exceptions;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ecommerce.app.payload.APIResponse;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->{
            // System.out.println("----------------------------------------");
            // System.out.println(error);
            // System.out.println("----------------------------------------");
            errorMap.put(error.getField(),error.getDefaultMessage());
        });

        return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);//400 Bad Request errorMap;
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>>handleConstraintViolationException(ConstraintViolationException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getConstraintViolations().forEach(error->{
            errorMap.put(error.getPropertyPath().toString(),error.getMessage());
        });

        return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);//400 Bad Request errorMap;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        APIResponse apiResponse = new APIResponse(ex.getMessage(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<APIResponse> ResourceExistsException(ResourceExistsException ex){
        APIResponse apiResponse = new APIResponse(ex.getMessage(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataPresentException.class)
    public ResponseEntity<APIResponse> DataNotPredent(NoDataPresentException ex){
        APIResponse apiResponse = new APIResponse(ex.getMessage(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }
}

