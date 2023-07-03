package br.com.lcn.goldenRaspberryAwards.controller;

import br.com.lcn.goldenRaspberryAwards.dto.PatchDTO;
import br.com.lcn.goldenRaspberryAwards.dto.ProducerWinnerFollowingDTO;
import br.com.lcn.goldenRaspberryAwards.exception.BadRequestException;
import br.com.lcn.goldenRaspberryAwards.exception.NotFoundException;
import br.com.lcn.goldenRaspberryAwards.model.producer.Producer;
import br.com.lcn.goldenRaspberryAwards.service.ProducerService;
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
@RequestMapping("producer")
public class
ProducerController {

    @Autowired
    private ProducerService producerService;

    @ApiOperation("Buscar todos os Produtores")
    @GetMapping(produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retornou lista de produtores com sucesso"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<List<Producer>> getAll() {
        return ResponseEntity.ok(producerService.getAll());
    }

    @ApiOperation("Buscar Produtor pelo id")
    @GetMapping(path = "/{id}", produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retornou produtor com sucesso"),
            @ApiResponse(code = 404, message = "Produtor não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Producer> findById(@PathVariable("id") Integer id) throws NotFoundException {
            return ResponseEntity.ok(this.producerService.findById(id));
    }


    @ApiOperation("Lista produtores com menor e maior intervalo de premiação consecutiva")
    @GetMapping(path = "winner", produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retornou lista com sucesso", response = ProducerWinnerFollowingDTO.class),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<ProducerWinnerFollowingDTO> getProducersWinner() {
        return ResponseEntity.ok(this.producerService.getProducersWinner());
    }


    @DeleteMapping("/{id}")
    @ApiOperation("Exclui um Produtor")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Produtor excluido com sucesso", response = void.class),
            @ApiResponse(code = 400, message = "Não é possivel excluir produtor que possui filmes associados"),
            @ApiResponse(code = 404, message = "Produtor não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Producer> deleteById(@PathVariable("id") Integer id) throws NotFoundException, BadRequestException {

            this.producerService.deleteById(id);
            return ResponseEntity.noContent().build();

    }

    @PostMapping(consumes = "application/json")
    @ApiOperation("Cadastra um novo Produtor")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produtor cadastrado com sucesso"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Producer> create(@Valid @RequestBody Producer producer) {
            Producer producerReturn = this.producerService.save(producer);
            return ResponseEntity.created(URI.create("/producer/" + producerReturn.getId()))
                    .body(producerReturn);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @ApiOperation("Edita um Produtor existente através do id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Produtor editado com sucesso", response = void.class),
            @ApiResponse(code = 404, message = "Produtor não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Producer> update(@Valid @RequestBody Producer producer,
                                           @PathVariable("id") Integer id) throws NotFoundException {

            return ResponseEntity.ok(this.producerService.update(producer, id));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    @ApiOperation("Edita um Produtor existente através do id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Produtor editado com sucesso", response = void.class),
            @ApiResponse(code = 400, message = "Erro na requisição"),
            @ApiResponse(code = 404, message = "Produtor não localizado"),
            @ApiResponse(code = 500, message = "Ocorerram erros na execução")
    })
    public ResponseEntity<Producer> updatePartial(@Valid @RequestBody PatchDTO patchDTO,
                                                  @PathVariable("id") Integer id) throws BadRequestException, NotFoundException {
            return ResponseEntity.ok(this.producerService.updatePartial(patchDTO, id));
    }


}

