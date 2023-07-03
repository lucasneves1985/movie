package br.com.lcn.goldenRaspberryAwards.exception.handler;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorDetails {
    private String title;
    private int status;
    private String details;
    private String developerMessage;
}
