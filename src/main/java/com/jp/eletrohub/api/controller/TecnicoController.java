package com.jp.eletrohub.api.controller;

import com.jp.eletrohub.api.dto.TecnicoDTO;
import com.jp.eletrohub.exception.RegraNegocioException;
import com.jp.eletrohub.model.entity.Tecnico;
import com.jp.eletrohub.service.TecnicoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {

    private final TecnicoService service;

    public TecnicoController(TecnicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity get() {
        List<Tecnico> clientes = service.getTecnicos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tecnico> cliente = service.getTecnicoById(id);
        if (!cliente.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente.get());
    }

    @PostMapping
    public ResponseEntity post(@RequestBody TecnicoDTO dto) {
        try {
            Tecnico tecnico = converter(dto);
            tecnico = service.salvar(tecnico);
            return ResponseEntity.status(201).body(tecnico);
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
