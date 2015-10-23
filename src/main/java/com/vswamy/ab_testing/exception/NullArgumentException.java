package com.vswamy.ab_testing.exception;

public class NullArgumentException extends IllegalArgumentException
{
    private static final long serialVersionUID = -1334570642250162384L;

    public NullArgumentException()
    {
        super();
    }

    public NullArgumentException(String message)
    {
        super(message);
    }

    public NullArgumentException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NullArgumentException(Throwable cause)
    {
        super(cause);
    }
}
