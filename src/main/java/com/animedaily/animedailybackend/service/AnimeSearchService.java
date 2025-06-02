package com.animedaily.animedailybackend.service;

import com.animedaily.animedailybackend.dto.AnimeSearchCriteria;
import com.animedaily.animedailybackend.model.Anime;
import com.animedaily.animedailybackend.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AnimeSearchService {

    @Autowired
    private AnimeRepository animeRepository;

    public Page<Anime> searchAnimes(AnimeSearchCriteria criteria, Pageable pageable) {
        Specification<Anime> spec = AnimeSpecifications.withSearchCriteria(criteria);
        
        return animeRepository.findAll(spec, pageable);
    }
}