package br.com.lcn.goldenRaspberryAwards.service;

import br.com.lcn.goldenRaspberryAwards.GoldenRaspberryAwardsApplication;
import br.com.lcn.goldenRaspberryAwards.dto.PatchDTO;
import br.com.lcn.goldenRaspberryAwards.dto.ProducerWinnerDTO;
import br.com.lcn.goldenRaspberryAwards.dto.ProducerWinnerFollowingDTO;
import br.com.lcn.goldenRaspberryAwards.exception.BadRequestException;
import br.com.lcn.goldenRaspberryAwards.exception.NotFoundException;
import br.com.lcn.goldenRaspberryAwards.model.movie.Movie;
import br.com.lcn.goldenRaspberryAwards.model.movie.MovieRepository;
import br.com.lcn.goldenRaspberryAwards.model.producer.Producer;
import br.com.lcn.goldenRaspberryAwards.model.producer.ProducerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProducerService {

    private static final Logger logger = LoggerFactory.getLogger(GoldenRaspberryAwardsApplication.class);

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private MovieRepository movieRepository;


    public Producer save(Producer producer) {
        return producerRepository.save(producer);
    }

    public List<Producer> createProducerWithString(String producerString) {
        List<Producer> producerList = new ArrayList<>();
        producerString = producerString.replace(" and ", ",");
        String[] producesVector = producerString.split(",");
        for (String name : producesVector) {
            if (!name.trim().equals("")) {
                Producer producer = producerRepository.findByName(name.trim());
                if (producer != null) {
                    producerList.add(producer);
                } else {
                    producer = Producer.builder().name(name.trim()).build();
                    producerRepository.save(producer);
                    producerList.add(producer);

                }
            }
        }
        return producerList;
    }


    public List<Producer> getAll() {
        return this.producerRepository.findAll();
    }

    public void deleteById(Integer id) throws NotFoundException, BadRequestException {
        Producer producer = this.producerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Produtor não localizado com o id: " + id)
        );
        if (producer.getMovies() != null) {
            if (producer.getMovies().size() > 0)
                throw new BadRequestException("Produtor possui filmes relacionados");
        }
        this.producerRepository.deleteById(id);
    }

    public Producer findById(Integer id) throws NotFoundException {
        return this.producerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Filme não localizado com o id: " + id)
        );
    }

    public Producer updatePartial(PatchDTO patchDTO, Integer id) throws BadRequestException, NotFoundException {
        try {
            Producer producer = this.producerRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Filme não localizado com o id: " + id)
            );
            if (patchDTO.getField().equals("name"))
                producer.setName(patchDTO.getValue());
            else {
                throw new BadRequestException("Campo: " + patchDTO.getField() + " é invalido");
            }
            this.producerRepository.save(producer);
            return producer;
        } catch (TransactionSystemException e) {
            logger.error(e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public Producer update(Producer producer, Integer id) throws NotFoundException {
        Producer producerDB = this.producerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Filme não localizado com o id: " + id)
        );
        BeanUtils.copyProperties(producer, producerDB, "id");
        return this.producerRepository.save(producerDB);
    }

    public ProducerWinnerFollowingDTO getProducersWinner() {
        Set<Producer> producerSet = new HashSet<>();
        List<ProducerWinnerDTO> producerWinnerDTOList = new ArrayList<>();
        ProducerWinnerFollowingDTO producerWinnerFollowingDTO = new ProducerWinnerFollowingDTO();
        int minInterval = -1;
        int maxInterval = -1;

        this.movieRepository.findByWinnerIsTrueOrderByLaunchYear()
                .forEach(movie -> producerSet.addAll(movie.getProducers()));


        for (Producer producer : producerSet) {
            int previousWin = -1;
            int followingWin;
            List<Movie> movieByProducer = producer.getMovies().stream().sorted(
                    Comparator.comparing(Movie::getLaunchYear)).collect(Collectors.toList());
            for (Movie movie : movieByProducer) {
                if (movie.getWinner()) {
                    if (previousWin == -1)
                        previousWin = movie.getLaunchYear();
                    else {
                        followingWin = movie.getLaunchYear();
                        int interval = followingWin - previousWin;
                        minInterval = minInterval == -1 ? interval : Math.min(minInterval, interval);
                        maxInterval = maxInterval == -1 ? interval : Math.max(maxInterval, interval);

                        producerWinnerDTOList.add(ProducerWinnerDTO.builder()
                                .followingWin(followingWin)
                                .previousWin(previousWin)
                                .interval(interval)
                                .producer(producer.getName()).build());

                        previousWin = movie.getLaunchYear();
                    }
                }
            }
        }

        int finalMinInterval = minInterval;
        producerWinnerFollowingDTO.getMin().addAll(
                producerWinnerDTOList.stream().filter(p -> p.getInterval().equals(finalMinInterval)).collect(Collectors.toList()));

        int finalMaxInterval = maxInterval;
        producerWinnerFollowingDTO.getMax().addAll(
                producerWinnerDTOList.stream().filter(p -> p.getInterval().equals(finalMaxInterval)).collect(Collectors.toList()));

        return producerWinnerFollowingDTO;
    }


}
