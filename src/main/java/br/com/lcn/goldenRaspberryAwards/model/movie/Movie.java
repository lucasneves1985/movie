package br.com.lcn.goldenRaspberryAwards.model.movie;

import br.com.lcn.goldenRaspberryAwards.model.producer.Producer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Ano é obrigatorio")
    @Min(value = 1888, message = "Ano minimo é 1888")
    private Integer launchYear;

    @NotBlank(message = "Titulo deve ser informado")
    private String title;

    @NotBlank(message = "Estudio deve ser informado")
    private String studios;

    private Boolean winner;

    @ManyToMany
    @JoinTable(name = "producer_has_movie", joinColumns = {@JoinColumn(name="movie_id")},
            inverseJoinColumns = {@JoinColumn(name="producer_id")})
    private List<Producer> producers;


}
