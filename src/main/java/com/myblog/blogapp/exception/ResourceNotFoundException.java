package com.myblog.blogapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{
    private String resourceName;//table name,post
    private String fieldName;//means id
    private long fieldValue;//1
    //Post not found with id:1
    public ResourceNotFoundException(String resourceName,String filedName,long fieldValue)
    {
        //this msg is automatically print by spring boot on postman when record is not found
        super(String.format("%s not found with %s : '%s'", resourceName, filedName, fieldValue));
        this.resourceName=resourceName;
        this.fieldName=filedName;
        this.fieldValue=fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public long getFieldValue() {
        return fieldValue;
    }
}
