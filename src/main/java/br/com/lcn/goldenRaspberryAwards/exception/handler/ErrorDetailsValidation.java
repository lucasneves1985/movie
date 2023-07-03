package br.com.lcn.goldenRaspberryAwards.exception.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class ErrorDetailsValidation {
        private String title;
        private int status;
        private String details;
        private Set<String> field = new HashSet<>();
        private Set<String> validationMessage = new HashSet<>();

}
