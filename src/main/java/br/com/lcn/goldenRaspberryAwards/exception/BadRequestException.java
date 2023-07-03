package br.com.lcn.goldenRaspberryAwards.exception;

public class BadRequestException extends Exception {
    public BadRequestException(String errorMessage) {
        super(errorMessage);
    }
    public BadRequestException() {

    }
}
