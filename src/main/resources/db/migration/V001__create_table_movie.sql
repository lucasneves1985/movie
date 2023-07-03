CREATE TABLE movie (
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    launch_Year INT NOT NULL,
    title       VARCHAR(80) NOT NULL,
    studios     VARCHAR(80) NOT NULL,
    winner      BOOLEAN
);

CREATE TABLE producer (
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(80) NOT NULL
);

CREATE TABLE producer_has_movie (
    movie_id    INT NOT NULL,
    producer_id INT NOT NULL,
    CONSTRAINT movie_fk FOREIGN KEY (movie_id) REFERENCES movie(id),
    CONSTRAINT producer_fk FOREIGN KEY (producer_id) REFERENCES producer(id)
)