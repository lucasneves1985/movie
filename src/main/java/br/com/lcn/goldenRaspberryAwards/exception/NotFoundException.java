package br.com.lcn.goldenRaspberryAwards.exception;

public class NotFoundException extends Exception {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundException() {

    }
}
