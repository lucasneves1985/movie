package br.com.lcn.goldenRaspberryAwards.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProducerWinnerFollowingDTO {
    private List<ProducerWinnerDTO> min = new ArrayList<>();
    private List<ProducerWinnerDTO> max = new ArrayList<>();

}
