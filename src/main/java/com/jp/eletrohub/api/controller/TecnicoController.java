package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.TecnicoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @GetMapping
    public ResponseEntity<List<TecnicoDTO>> list() {
        var tecnicos = tecnicoService.list().stream().map(TecnicoDTO::create).toList();
        return ResponseEntity.ok(tecnicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoDTO> get(@PathVariable("id") Long id) {
        var tecnico = tecnicoService.findById(id).map(TecnicoDTO::create).orElseThrow(() -> new RegraNegocioException("Técnico não encontrado"));
        return ResponseEntity.ok(tecnico);
    }

    @PostMapping()
    public ResponseEntity<Object> post(@RequestBody TecnicoDTO dto) {
        try {
            Tecnico tecnico = converter(dto);
            tecnico = tecnicoService.save(tecnico);
            return ResponseEntity.status(HttpStatus.CREATED).body(TecnicoDTO.create(tecnico));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody TecnicoDTO dto) {
        if (tecnicoService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Tecnico tecnico = converter(dto);
            tecnico.setId(id);
            tecnicoService.save(tecnico);
            return ResponseEntity.ok(tecnico);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long id) {
        var tecnico = tecnicoService.findById(id);
        if (tecnico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            tecnicoService.delete(tecnico.get());
            return ResponseEntity.ok().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Tecnico converter(TecnicoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Tecnico.class);
    }
}
