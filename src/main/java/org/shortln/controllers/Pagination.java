package org.shortln.controllers;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.UnaryOperator;

@Data
@Builder
public class Pagination<T> {
    private Long count;
    private List<T> items;

    @Data
    @NoArgsConstructor
    public static class Query<T> {
        private String str;
        private Integer page = 0;
        private Integer size = 10;

        public Pageable getPageable() {
            return PageRequest.of(page, size);
        }

        public <ID, R extends JpaRepository<T, ID>>Pagination<T> getPagination(R repo, Example<T> example) {
            return this.getPagination(repo, example, p -> p);
        }

        public <ID, R extends JpaRepository<T, ID>>Pagination<T> getPagination(
                R repo, Example<T> example, UnaryOperator<Page<T>> transformPage
        ) {
            return new Pagination<>(
                    repo.count(example), transformPage.apply(repo.findAll(example, getPageable())).toList()
            );
        }
    }
}
