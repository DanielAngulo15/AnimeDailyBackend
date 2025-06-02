package com.animedaily.animedailybackend.service;

import com.animedaily.animedailybackend.dto.AnimeSearchCriteria;
import com.animedaily.animedailybackend.model.Anime;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

public class AnimeSpecifications {

    public static Specification<Anime> withSearchCriteria(AnimeSearchCriteria criteria) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (criteria.getGenero() != null && !criteria.getGenero().isEmpty()) {
                for (String genero : criteria.getGenero()) {
                    predicate = cb.and(predicate, 
                        cb.like(cb.lower(root.get("genero")), "%" + genero.toLowerCase() + "%")
                    );
                }
            }

            if (criteria.getAnio() != null) {
                predicate = cb.and(predicate, 
                    cb.equal(root.get("anioEstreno"), criteria.getAnio())
                );
            }

            if (criteria.getTemporada() != null) {
                predicate = cb.and(predicate, 
                    cb.equal(cb.lower(root.get("temporadaEstreno")), criteria.getTemporada().toLowerCase())
                );
            }

            if (criteria.getTipo() != null) {
                predicate = cb.and(predicate, 
                    cb.equal(cb.lower(root.get("tipo")), criteria.getTipo().toLowerCase())
                );
            }

            if (criteria.getEstado() != null) {
                predicate = cb.and(predicate, 
                    cb.equal(cb.lower(root.get("estado")), criteria.getEstado().toLowerCase())
                );
            }

            if (criteria.getPuntuacionMinima() != null) {
                predicate = cb.and(predicate, 
                    cb.greaterThanOrEqualTo(root.get("puntuacion"), criteria.getPuntuacionMinima())
                );
            }

            if (criteria.getClasificacion() != null) {
                predicate = cb.and(predicate, 
                    cb.equal(cb.lower(root.get("clasificacion")), criteria.getClasificacion().toLowerCase())
                );
            }

            if (criteria.getFormato() != null) {
                predicate = cb.and(predicate, 
                    cb.equal(cb.lower(root.get("tipo")), criteria.getFormato().toLowerCase())
                );
            }

            return predicate;
        };
    }
}