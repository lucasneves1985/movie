package br.com.lcn.goldenRaspberryAwards.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchDTO {
    private String field;
    private String value;

}
