package com.demo.springboot.employee.exception;

public class NullPointerExceptions extends NullPointerException {
    private static final long serialVersionUID = 1L;

    public NullPointerExceptions() {
        super();
    }

    public NullPointerExceptions(final String message) {
        super(message);
    }

}