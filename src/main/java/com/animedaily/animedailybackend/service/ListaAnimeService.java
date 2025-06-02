package com.animedaily.animedailybackend.service;

import com.animedaily.animedailybackend.dto.ListaAnimeDTO;
import com.animedaily.animedailybackend.mapper.ListaAnimeMapper;
import com.animedaily.animedailybackend.model.Anime;
import com.animedaily.animedailybackend.model.ListaAnime;
import com.animedaily.animedailybackend.repository.AnimeRepository;
import com.animedaily.animedailybackend.repository.ListaAnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListaAnimeService {

    private final ListaAnimeRepository listaAnimeRepository;
    private final ListaAnimeMapper mapper;
    private final AnimeRepository animeRepository;

    public List<ListaAnimeDTO> obtenerListasPorUsuario(Long usuarioId) {
        return listaAnimeRepository.findByUsuarioId(usuarioId).stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
    }

    public ListaAnimeDTO crearLista(ListaAnimeDTO listaAnimeDTO) {
        var entity = mapper.toEntity(listaAnimeDTO);
        var saved = listaAnimeRepository.save(entity);
        return mapper.toDTO(saved);
    }

    public Optional<ListaAnimeDTO> obtenerListaPorId(Long id) {
        return listaAnimeRepository.findById(id)
            .map(mapper::toDTO);
    }

    public void eliminarLista(Long id) {
        listaAnimeRepository.deleteById(id);
    }

    public ListaAnimeDTO actualizarLista(Long id, ListaAnimeDTO listaActualizadaDTO) {
        return listaAnimeRepository.findById(id)
            .map(existing -> {
                existing.setNombreLista(listaActualizadaDTO.getNombreLista());
                existing.setAnimes(mapper.toEntity(listaActualizadaDTO).getAnimes());
                var updated = listaAnimeRepository.save(existing);
                return mapper.toDTO(updated);
            })
            .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
    }

        public ListaAnimeDTO añadirAnimeALista(Long listaId, Long animeId) {
        ListaAnime lista = listaAnimeRepository.findById(listaId)
            .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
        
        Anime anime = animeRepository.findById(animeId)
            .orElseThrow(() -> new RuntimeException("Anime no encontrado"));
        
        if (!lista.getAnimes().contains(anime)) {
            lista.getAnimes().add(anime);
            listaAnimeRepository.save(lista);
        }
        
        return mapper.toDTO(lista);
    }

    public ListaAnimeDTO eliminarAnimeDeLista(Long listaId, Long animeId) {
        ListaAnime lista = listaAnimeRepository.findById(listaId)
            .orElseThrow(() -> new RuntimeException("Lista no encontrada"));
        
        Anime anime = animeRepository.findById(animeId)
            .orElseThrow(() -> new RuntimeException("Anime no encontrado"));
        
        lista.getAnimes().remove(anime);
        listaAnimeRepository.save(lista);
        
        return mapper.toDTO(lista);
    }
}
