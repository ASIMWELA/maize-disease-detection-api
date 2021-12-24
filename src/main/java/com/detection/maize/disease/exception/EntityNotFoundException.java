package com.detection.maize.disease.exception;

public class EntityNotFoundException extends RuntimeException
{
    public EntityNotFoundException()
    {
        super();
    }

    public EntityNotFoundException(String message)
    {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause)
    {
        super(cause);
    }
}
