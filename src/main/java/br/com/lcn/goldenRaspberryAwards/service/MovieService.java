package br.com.lcn.goldenRaspberryAwards.service;

import br.com.lcn.goldenRaspberryAwards.GoldenRaspberryAwardsApplication;
import br.com.lcn.goldenRaspberryAwards.dto.PatchDTO;
import br.com.lcn.goldenRaspberryAwards.exception.BadRequestException;
import br.com.lcn.goldenRaspberryAwards.exception.NotFoundException;
import br.com.lcn.goldenRaspberryAwards.model.movie.Movie;
import br.com.lcn.goldenRaspberryAwards.model.movie.MovieRepository;
import br.com.lcn.goldenRaspberryAwards.model.producer.Producer;
import br.com.lcn.goldenRaspberryAwards.utils.FileFieldIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service
public class MovieService {

    private static final Logger logger = LoggerFactory.getLogger(GoldenRaspberryAwardsApplication.class);
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ProducerService producerService;


    public Movie save(Movie movie) throws BadRequestException {
        for (Producer producer : movie.getProducers()) {
            if (producer.getId() == null)
                throw new BadRequestException("Informado um produtor não cadastrado");
        }
        return movieRepository.save(movie);
    }

    public Movie createMovieWithVector(String[] info, FileFieldIndex fileFieldIndex) {
        try {

            return Movie.builder()
                    .launchYear(Integer.parseInt(info[fileFieldIndex.getIndexYear()]))
                    .title(info[fileFieldIndex.getIndexTitle()])
                    .studios(info[fileFieldIndex.getIndexStudio()])
                    .producers(producerService.createProducerWithString(info[fileFieldIndex.getIndexProducers()]))
                    .winner(info.length == 5
                            && info[fileFieldIndex.getIndexWinner()].equalsIgnoreCase("yes"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("****Errors occurred when trying to save the title: " + info[1]);
            return null;
        }

    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadMovieOnCSV() {
        logger.info("Start import");
        try {
            BufferedReader br = this.loadFileCSV();
            if (br != null) {
                String line = br.readLine();
                FileFieldIndex fileFieldIndex = getHeaderFile(line);
                line = isHeader(line) ? br.readLine() : line;

                while (line != null) {
                    if (!line.trim().equals("")) {
                        String[] info = line.split(";");
                        Movie movie = this.createMovieWithVector(info, fileFieldIndex);
                        if (movie != null)
                            this.save(movie);
                    }
                    line = br.readLine();
                }
            } else {
                throw new Exception("*** BOOT FILE NOT FOUND ***");
            }
        } catch (Exception e) {
            logger.error("****** ERRORS HAVE OCCURRED WHEN LOADING THE INITIALIZATION FILE ******");
            logger.error("***" + e.getMessage() + "***");
        } finally {
            logger.info("Finish import");
        }

    }

    private BufferedReader loadFileCSV() {
        if (this.getClass().getClassLoader().getResourceAsStream("movielist.csv") != null) {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("movielist.csv");
            assert in != null;
            InputStreamReader streamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            return new BufferedReader(streamReader);
        } else {
            return null;
        }
    }

    public List<Movie> getAll() {
        return this.movieRepository.findAll();
    }

    public void deleteById(Integer id) throws NotFoundException {
        this.movieRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Filme não localizado com o id: " + id)
        );
        this.movieRepository.deleteById(id);
    }

    public Movie findById(Integer id) throws NotFoundException {
        return this.movieRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Filme não localizado com o id: " + id)
        );
    }

    public Movie updatePartial(PatchDTO patchDTO, Integer id) throws BadRequestException, NotFoundException {
        try {
            Movie movie = this.movieRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Filme não localizado com o id: " + id)
            );

            switch (patchDTO.getField()) {
                case "launchYear":
                    movie.setLaunchYear(Integer.parseInt(patchDTO.getValue()));
                    break;
                case "studios":
                    movie.setStudios(patchDTO.getValue());
                    break;
                case "title":
                    movie.setTitle(patchDTO.getValue());
                    break;
                case "winner":
                    movie.setWinner(Boolean.parseBoolean(patchDTO.getValue()));
                    break;
                default:
                    throw new BadRequestException("Campo: " + patchDTO.getField() + " é invalido");
            }
            return this.save(movie);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Informações invalidas");
        }
    }

    public Movie update(Movie movie, Integer id) throws BadRequestException, NotFoundException {
        Movie movieDB = this.movieRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Filme não localizado com o id: " + id));
        BeanUtils.copyProperties(movie, movieDB, "id");
        return this.save(movieDB);
    }

    public FileFieldIndex getHeaderFile(String header) {
        if (header.lastIndexOf("year") >= 0 && header.lastIndexOf("title") >= 0
                && header.lastIndexOf("studios") >= 0 && header.lastIndexOf("winner") >= 0
                && header.lastIndexOf("producers") >= 0) {

            String[] headers = header.split(";");

            FileFieldIndex fileFieldIndex = new FileFieldIndex();
            for (int i = 0; i < headers.length; i++) {
                switch (headers[i]) {
                    case "year":
                        fileFieldIndex.setIndexYear(i);
                        break;
                    case "title":
                        fileFieldIndex.setIndexTitle(i);
                        break;
                    case "studios":
                        fileFieldIndex.setIndexStudio(i);
                        break;
                    case "producers":
                        fileFieldIndex.setIndexProducers(i);
                        break;
                    case "winner":
                        fileFieldIndex.setIndexWinner(i);
                        break;
                }
            }
            return fileFieldIndex;
        } else {
            return new FileFieldIndex();
        }
    }

    private boolean isHeader(String line) {

        return (line.lastIndexOf("year") >= 0 && line.lastIndexOf("title") >= 0
                && line.lastIndexOf("studios") >= 0 && line.lastIndexOf("winner") >= 0
                && line.lastIndexOf("producers") >= 0);

    }
}
