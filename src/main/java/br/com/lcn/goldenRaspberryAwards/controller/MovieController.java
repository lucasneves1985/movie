package br.com.lcn.goldenRaspberryAwards.controller;

import br.com.lcn.goldenRaspberryAwards.dto.PatchDTO;
import br.com.lcn.goldenRaspberryAwards.exception.BadRequestException;
import br.com.lcn.goldenRaspberryAwards.exception.NotFoundException;
import br.com.lcn.goldenRaspberryAwards.model.movie.Movie;
import br.com.lcn.goldenRaspberryAwards.service.MovieService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @ApiOperation("Buscar todos os Filmes")
    @GetMapping(produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retornou lista de filmes com sucesso"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<List<Movie>> getAll() throws BadRequestException {
        return ResponseEntity.ok(movieService.getAll());
    }

    @ApiOperation("Buscar Filme pelo id")
    @GetMapping(path = "/{id}", produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retornou filme com sucesso"),
            @ApiResponse(code = 404, message = "Filme não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Movie> findById(@PathVariable("id") Integer id) throws NotFoundException {
            return ResponseEntity.ok(this.movieService.findById(id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Exclui um Filme")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Filme excluido com sucesso", response = void.class),
            @ApiResponse(code = 404, message = "Filme não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) throws NotFoundException {
                this.movieService.deleteById(id);
                return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = "application/json")
    @ApiOperation("Cadastra um novo Filme")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Filme cadastrado com sucesso"),
            @ApiResponse(code = 400, message = "Erro na requisição"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Movie> create(@Valid @RequestBody Movie movie) throws BadRequestException {
            Movie movieReturn = this.movieService.save(movie);
            return ResponseEntity.created(URI.create("/movie/" + movieReturn.getId())).body(movieReturn);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @ApiOperation("Edita um Filme existente através do id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Filme editado com sucesso", response = Movie.class),
            @ApiResponse(code = 400, message = "Erro na requisição"),
            @ApiResponse(code = 404, message = "Filme não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Movie> update(@Valid @RequestBody Movie movie, @PathVariable("id") Integer id)
            throws BadRequestException, NotFoundException {

            return ResponseEntity.ok(this.movieService.update(movie, id));

    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    @ApiOperation("Edita um Filme existente através do id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Filme editado com sucesso", response = Movie.class),
            @ApiResponse(code = 400, message = "Erro na requisição"),
            @ApiResponse(code = 404, message = "Filme não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Movie> updatePartial(@Valid @RequestBody PatchDTO patchDTO, @PathVariable("id") Integer id)
            throws BadRequestException, NotFoundException {

            return ResponseEntity.ok(this.movieService.updatePartial(patchDTO, id));

    }


}
