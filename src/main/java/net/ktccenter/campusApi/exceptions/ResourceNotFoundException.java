package net.ktccenter.campusApi.exceptions;

import org.webjars.NotFoundException;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String message){
        super(message);
    }
}
