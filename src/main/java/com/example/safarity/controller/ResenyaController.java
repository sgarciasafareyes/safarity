package com.example.safarity.controller;

import com.example.safarity.converter.ResenyaMapper;
import com.example.safarity.dto.ResenyaDTO;
import com.example.safarity.service.ResenyaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController()
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/resenya")
public class ResenyaController {

    @Autowired
    private ResenyaService resenyaService;

    @Autowired
    private ResenyaMapper resenyaMapper;


    @GetMapping(value = "/listar")
    public List<ResenyaDTO> listarResenya() {
        return resenyaService.listarResenya();
    }


    @PostMapping(value = "/crear")
    public ResenyaDTO crearResenya(@RequestBody ResenyaDTO resenyaDTO) {
        return resenyaService.crearResenya(resenyaDTO);
    }


    @PutMapping(value = "/modificar")
    public ResenyaDTO modificarResenya(@RequestBody ResenyaDTO resenyaDTO) {
        return resenyaMapper.toDTO(resenyaService.modificarResenya(resenyaDTO));
    }


    @DeleteMapping(value = "/eliminar")
    public String eliminarResenya(@RequestBody ResenyaDTO resenyaDTO) {
        return resenyaService.eliminarResenya(resenyaDTO);
    }
}
