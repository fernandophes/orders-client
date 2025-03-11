package br.edu.ufersa.cc.sd.exceptions;

public abstract class CustomException extends RuntimeException {

    CustomException(final String message) {
        super(message);
    }

    CustomException(final String message, final Throwable cause) {
        super(message, cause);
    }

    CustomException(final Throwable cause) {
        super(cause);
    }

}
