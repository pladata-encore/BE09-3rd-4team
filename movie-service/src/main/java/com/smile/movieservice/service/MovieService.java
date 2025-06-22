package com.smile.movieservice.service;

import com.smile.movieservice.client.ReviewClient;
import com.smile.movieservice.dto.MovieRequest;
import com.smile.movieservice.dto.MovieResponse;
import com.smile.movieservice.entity.Actor;
import com.smile.movieservice.entity.Director;
import com.smile.movieservice.entity.Genre;
import com.smile.movieservice.entity.Movie;
import com.smile.movieservice.repository.ActorRepository;
import com.smile.movieservice.repository.DirectorRepository;
import com.smile.movieservice.repository.GenreRepository;
import com.smile.movieservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final ReviewClient reviewClient;

    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        // 1. 감독 조회 또는 저장
        Director director = directorRepository.findByName(request.getDirector().getName())
                .orElseGet(() -> directorRepository.save(
                        Director.builder().name(request.getDirector().getName()).build()
                ));

        // 2. 배우 조회 또는 저장
        Set<Actor> actors = new HashSet<>();
        if (request.getActors() != null) {
            for (MovieRequest.ActorRequest actorReq : request.getActors()) {
                Actor actor = actorRepository.findByName(actorReq.getName())
                        .orElseGet(() -> actorRepository.save(
                                Actor.builder().name(actorReq.getName()).build()
                        ));
                actors.add(actor);
            }
        }

        // 3. 장르 조회
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(request.getGenreIds()));

        // 4. 영화 저장
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .ageRating(request.getAgeRating())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .rating(request.getRating())
                .director(director)
                .actors(actors)
                .genres(genres)
                .build();

        Movie saved = movieRepository.save(movie);
        return mapToResponse(saved);
    }

    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화가 존재하지 않습니다."));

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(request.getGenreIds()));

        movie.setTitle(request.getTitle());
        movie.setAgeRating(request.getAgeRating());
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setRating(request.getRating());
        movie.setGenres(genres);
        // 감독, 배우는 update 시에는 생략 (필요하면 구현)

        return mapToResponse(movieRepository.save(movie));
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화가 존재하지 않습니다."));
        movieRepository.delete(movie);
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화가 존재하지 않습니다."));
        return mapToResponse(movie);
    }


    private MovieResponse mapToResponse(Movie movie) {
        List<String> genreNames = movie.getGenres().stream()
                .map(Genre::getName)
                .toList();

        List<String> actorNames = movie.getActors().stream()
                .map(Actor::getName)
                .toList();

        String directorName = movie.getDirector() != null ? movie.getDirector().getName() : null;

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .ageRating(movie.getAgeRating())  // ✅ 누락 보완
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .rating(movie.getRating())
                .genres(genreNames)
                .directorName(directorName)
                .actors(actorNames)
                .build();
    }

    public void recalculateAndUpdateAverageRating(Long movieId) {
        // 1. 리뷰 서비스에서 평점 목록을 가져옴
        List<Double> ratings = reviewClient.getRatingsByMovieId(movieId);
        if (ratings == null || ratings.isEmpty()) {
            log.warn("영화 ID {}에 대한 리뷰가 없습니다", movieId);
            return;
        }

        // 2. 영화 객체 조회
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("해당 영화가 존재하지 않습니다."));

        // 3. 영화 객체가 직접 평균 계산 & 반영
        movie.updateRatingFrom(ratings);

        // 4. DB 저장
        movieRepository.save(movie);
    }


}
