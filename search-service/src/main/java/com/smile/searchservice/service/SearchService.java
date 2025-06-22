package com.smile.searchservice.service;

import com.smile.searchservice.client.MovieClient;
import com.smile.searchservice.dto.SearchRequest;
import com.smile.searchservice.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MovieClient movieClient;

    // 전체 목록
    public List<SearchResponse> getAllMovies() {
        return movieClient.getMovies().getData();
    }

    // 단건 조회
    public SearchResponse getMovieById(Long id) {
        return movieClient.getMovie(id).getData();
    }

    // 영화 등록
    public SearchResponse createMovie(SearchRequest dto) {
        return movieClient.addMovie(dto).getData();
    }

    // 영화 수정
    public SearchResponse updateMovie(Long id, SearchRequest dto) {
        return movieClient.updateMovie(id, dto).getData();
    }

    // 영화 삭제
    public void deleteMovie(Long id) {
        movieClient.deleteMovie(id);
    }

    // 키워드 검색 (제목, 장르, 감독, 배우 포함)
    public List<SearchResponse> searchMoviesByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return getAllMovies().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(lowerKeyword)
                        || (m.getGenres() != null && m.getGenres().stream().anyMatch(g -> g.toLowerCase().contains(lowerKeyword)))
                        || (m.getDirectorName() != null && m.getDirectorName().toLowerCase().contains(lowerKeyword))
                        || (m.getActors() != null && m.getActors().stream().anyMatch(a -> a.toLowerCase().contains(lowerKeyword))))
                .collect(Collectors.toList());
    }

    // 장르별 평점순
    public List<SearchResponse> getMoviesByGenreAndRatingDesc(String genre) {
        return getAllMovies().stream()
                .filter(m -> m.getGenres() != null && m.getGenres().stream().anyMatch(g -> g.equalsIgnoreCase(genre)))
                .sorted(Comparator.comparing(SearchResponse::getRating).reversed())
                .collect(Collectors.toList());
    }

    // 전체 평점순
    public List<SearchResponse> getAllMoviesByRatingDesc() {
        return getAllMovies().stream()
                .sorted(Comparator.comparing(SearchResponse::getRating).reversed())
                .collect(Collectors.toList());
    }

    // 최신순 정렬
    public List<SearchResponse> getAllMoviesByReleaseDateDesc() {
        return getAllMovies().stream()
                .sorted(Comparator.comparing(SearchResponse::getReleaseDate).reversed())
                .collect(Collectors.toList());
    }

    // 배우별 영화 검색
    public List<SearchResponse> getMoviesByActor(String actorName) {
        return getAllMovies().stream()
                .filter(m -> m.getActors() != null && m.getActors().stream().anyMatch(a -> a.equalsIgnoreCase(actorName)))
                .collect(Collectors.toList());
    }

    // 감독별 영화 검색
    public List<SearchResponse> getMoviesByDirector(String directorName) {
        return getAllMovies().stream()
                .filter(m -> m.getDirectorName() != null && m.getDirectorName().equalsIgnoreCase(directorName))
                .collect(Collectors.toList());
    }

    // 제목으로 영화 검색
    public List<SearchResponse> getMoviesByTitle(String title) {
        return getAllMovies().stream()
                .filter(m -> m.getTitle() != null && m.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    // 장르별 영화 검색
    public List<SearchResponse> getMoviesByGenre(String genre) {
        return getAllMovies().stream()
                .filter(m -> m.getGenres() != null && m.getGenres().stream().anyMatch(g -> g.equalsIgnoreCase(genre)))
                .collect(Collectors.toList());
    }


    // 설명/줄거리 키워드 검색
    public List<SearchResponse> getMoviesByDescriptionKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return movieClient.getMovies().getData().stream()
                .filter(m -> m.getDescription() != null && m.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<SearchResponse> getMoviesByYear(int year) {
        return getAllMovies().stream()
                .filter(m -> {
                    if (m.getReleaseDate() == null) return false;
                    // "2022-07-01" → 2022
                    String[] parts = m.getReleaseDate().split("-");
                    if (parts.length < 1) return false;
                    return Integer.parseInt(parts[0]) == year;
                })
                .collect(Collectors.toList());
    }

    // 평점 min 이상
    public List<SearchResponse> getMoviesByMinRating(Double min) {
        return getAllMovies().stream()
                .filter(m -> m.getRating() != null && m.getRating() >= min)
                .collect(Collectors.toList());
    }

    // 제목, 감독, 설명 중 하나라도 keyword 포함
    public List<SearchResponse> getMoviesByAnyFieldKeyword(String keyword) {
        String lower = keyword.toLowerCase();
        return getAllMovies().stream()
                .filter(m -> (m.getTitle() != null && m.getTitle().toLowerCase().contains(lower))
                        || (m.getDirectorName() != null && m.getDirectorName().toLowerCase().contains(lower))
                        || (m.getDescription() != null && m.getDescription().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    // 최신 영화 1개 (개봉일 기준)
    public SearchResponse getLatestMovie() {
        return getAllMovies().stream()
                .filter(m -> m.getReleaseDate() != null)
                .max(Comparator.comparing(SearchResponse::getReleaseDate))
                .orElse(null);
    }

    // 가장 오래된 영화 1개 (개봉일 기준)
    public SearchResponse getOldestMovie() {
        return getAllMovies().stream()
                .filter(m -> m.getReleaseDate() != null)
                .min(Comparator.comparing(SearchResponse::getReleaseDate))
                .orElse(null);
    }

    public List<SearchResponse> getMoviesByTitleStart(String start) {
        String lower = start.trim().toLowerCase();
        return getAllMovies().stream()
                .filter(m -> m.getTitle() != null &&
                        m.getTitle().trim().toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }

    // 제목이 특정 단어로 끝나는 영화 검색
    public List<SearchResponse> getMoviesByTitleEnd(String end) {
        String lower = end.toLowerCase().trim();
        return getAllMovies().stream()
                .filter(m -> m.getTitle() != null && m.getTitle().toLowerCase().endsWith(lower))
                .collect(Collectors.toList());
    }

    public List<SearchResponse> getMoviesByAnyPerson(String keyword) {
        String lower = keyword.toLowerCase();
        return getAllMovies().stream()
                .filter(m ->
                        (m.getDirectorName() != null && m.getDirectorName().toLowerCase().contains(lower)) ||
                                (m.getActors() != null && m.getActors().stream().anyMatch(a -> a.toLowerCase().contains(lower)))
                )
                .collect(Collectors.toList());
    }

    public Long getMovieCount() {
        return (long)getAllMovies().size();
    }

    public List<SearchResponse> getMoviesByAgeRating(String rating) {
        return getAllMovies().stream()
                .filter(m -> m.getAgeRating() != null && m.getAgeRating().equals(rating))
                .collect(Collectors.toList());
    }

    public List<SearchResponse> getMoviesByMultipleAgeRatings(List<String> ratings) {
        return getAllMovies().stream()
                .filter(m -> m.getAgeRating() != null && ratings.contains(m.getAgeRating()))
                .collect(Collectors.toList());
    }

    public List<SearchResponse> getTopMoviesByAgeRating(String rating, int n) {
        return getAllMovies().stream()
                .filter(m -> m.getAgeRating() != null && m.getAgeRating().equals(rating))
                .sorted(Comparator.comparing(SearchResponse::getRating).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<SearchResponse> getMoviesByAgeAndGenre(String rating, String genre) {
        return getAllMovies().stream()
                .filter(m -> m.getAgeRating() != null && m.getAgeRating().equals(rating))
                .filter(m -> m.getGenres() != null && m.getGenres().stream().anyMatch(g -> g.equalsIgnoreCase(genre)))
                .collect(Collectors.toList());
    }

    public List<SearchResponse> getMoviesByTitleContains(String keyword) {
        String lower = keyword.toLowerCase().trim();
        System.out.println("[DEBUG] keyword=" + lower);
        List<SearchResponse> all = getAllMovies();
        for (SearchResponse s : all) {
            System.out.println("[DEBUG] title=" + s.getTitle());
        }
        return all.stream()
                .filter(m -> m.getTitle() != null && m.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    // 장르에 키워드 포함된 영화 검색 (부분일치)
    public List<SearchResponse> getMoviesByGenreContains(String keyword) {
        String lower = keyword.toLowerCase().trim();
        return getAllMovies().stream()
                .filter(m -> m.getGenres() != null &&
                        m.getGenres().stream()
                                .anyMatch(g -> g != null && g.toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    // 배우에 키워드 포함된 영화 검색 (부분일치)
    public List<SearchResponse> getMoviesByActorContains(String keyword) {
        String lower = keyword.toLowerCase().trim();
        return getAllMovies().stream()
                .filter(m -> m.getActors() != null && m.getActors().stream()
                        .anyMatch(a -> a != null && a.toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }
}