package com.example.safarity.controller;

import com.example.safarity.dto.UsuarioDTO;
import com.example.safarity.model.Usuario;
import com.example.safarity.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/listar")
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listar();
    }

    @PostMapping(value = "/crear")
    public UsuarioDTO crearUsuario(@RequestBody UsuarioDTO dto) {
        return usuarioService.crear(dto);
    }

    @PutMapping(value = "/modificar")
    public Usuario modificarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.modificarUsuario(usuarioDTO);
    }


}
