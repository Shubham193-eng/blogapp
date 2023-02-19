package com.myblog.blogapp.exception;

import com.myblog.blogapp.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice//any Exception occurs in the project spring boot gives it to this class by using this annotation
//this class will have the method which handle that exception.
public class  GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    //Specific exception(means if you are giving wrong id ,wrong pan card number)
    //this annotation will take exception object and passed it in handler method deffination.
    @ExceptionHandler(ResourceNotFoundException.class)//when ever the resourcenotfound exception class obj
    //created this method will handle that.
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
    WebRequest webRequest)//by using web request we get extra information give it to the postman.
    {
        //method store exception details it into the entity class object
       ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));//web request.getdisc provide uri dtails(in details section of entityin postma.
       return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllException(Exception exception,WebRequest webRequest)
    {
        //method store exception details it into the entity class object
        ErrorDetails errorDetails=new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));//web request.getdisc provide uri dtails(in details section of entityin postma.
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
