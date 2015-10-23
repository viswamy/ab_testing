package com.vswamy.ab_testing.exception;

public class ZeroLengthArgumentException extends IllegalArgumentException
{

    private static final long serialVersionUID = 835322863443804805L;

    public ZeroLengthArgumentException()
    {
        super();
    }

    public ZeroLengthArgumentException(String message)
    {
        super(message);
    }

    public ZeroLengthArgumentException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ZeroLengthArgumentException(Throwable cause)
    {
        super(cause);
    }
}
