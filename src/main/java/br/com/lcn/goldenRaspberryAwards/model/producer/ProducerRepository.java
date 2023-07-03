package br.com.lcn.goldenRaspberryAwards.model.producer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Integer> {
    Producer findByName(String name);
}
