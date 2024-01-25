package net.ktccenter.campusApi.exceptions;

public class InternalException extends InternalError{

    private static final long serialVersionUID = 1L;

    public InternalException(String message){
        super(message);
    }
}
