package fr.neutronstars.neuronal.network.api.exception;

public class MatrixRuntimeException extends RuntimeException
{
    public MatrixRuntimeException()
    {
        super();
    }

    public MatrixRuntimeException(String message)
    {
        super(message);
    }

    public MatrixRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MatrixRuntimeException(Throwable cause)
    {
        super(cause);
    }

    protected MatrixRuntimeException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
