package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.ClienteDTO;
import com.jp.eletrohub.api.dto.GerenteDTO;
import com.jp.eletrohub.api.dto.TecnicoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.service.TecnicoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {

    private final TecnicoService service;

    @GetMapping
    public ResponseEntity get() {
        List<Tecnico> tecnicos = service.getTecnicos();
        return ResponseEntity.ok(tecnicos.stream().map(TecnicoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tecnico> tecnico = service.getTecnicoById(id);
        if (!tecnico.isPresent()) {
            return new ResponseEntity("Gerente não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tecnico.map(TecnicoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody TecnicoDTO dto) {
        try {
            Tecnico tecnico = converter(dto);
            tecnico = service.salvar(tecnico);
            return new ResponseEntity(tecnico, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TecnicoDTO dto) {
        if (!service.getTecnicoById(id).isPresent()) {
            return new ResponseEntity("Técnico não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Tecnico tecnico = converter(dto);
            tecnico.setId(id);
            service.salvar(tecnico);
            return ResponseEntity.ok(tecnico);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Tecnico> tecnico = service.getTecnicoById(id);
        if (!tecnico.isPresent()) {
            return new ResponseEntity("Técnico não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tecnico.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Tecnico converter(TecnicoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Tecnico tecnico = modelMapper.map(dto, Tecnico.class);
        return tecnico;
    }
}
