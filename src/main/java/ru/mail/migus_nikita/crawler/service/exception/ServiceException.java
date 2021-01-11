package ru.mail.migus_nikita.crawler.service.exception;

/**
 * Thrown when an error in services occurred.
 *
 * @version 1.0
 * @since 2020-07-06
 */

public class ServiceException extends Exception {
    /**
     * Constructs a ServiceException with no detail message.
     */
    public ServiceException() {
    }

    /**
     * Constructs a ServiceException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a ServiceException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a ServiceException with the specified cause.
     *
     * @param cause the cause.
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
