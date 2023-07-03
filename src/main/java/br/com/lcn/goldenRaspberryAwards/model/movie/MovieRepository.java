package br.com.lcn.goldenRaspberryAwards.model.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByWinnerIsTrueOrderByLaunchYear();

    List<Movie> findByWinnerIsTrueAndProducers_idOrderByLaunchYear(Integer id);

    Movie findByWinnerIsTrueAndLaunchYear(Integer launchYear);
}
