package com.demo.springboot.employee.exception;

import java.util.NoSuchElementException;

public class ResourceNotFoundException extends NoSuchElementException {

    private static final long serialVersionUID = -9079454849611061074L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

}