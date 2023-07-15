package com.bilpa.android.services;

public class IllegalEntityException extends Exception {

    private static final long serialVersionUID = 778612152601035071L;

    public IllegalEntityException() {
        super();
    }

    public IllegalEntityException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IllegalEntityException(String detailMessage) {
        super(detailMessage);
    }

    public IllegalEntityException(Throwable throwable) {
        super(throwable);
    }
}
