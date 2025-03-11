package br.edu.ufersa.cc.sd.exceptions;

public class OperationException extends CustomException {

    public OperationException(final String message) {
        super(message);
    }

    public OperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
