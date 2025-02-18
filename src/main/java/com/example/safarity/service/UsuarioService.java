package com.example.safarity.service;

import com.example.safarity.converter.UsuarioMapper;
import com.example.safarity.dto.ParticipanteDTO;
import com.example.safarity.dto.UsuarioDTO;
import com.example.safarity.model.Participante;
import com.example.safarity.model.Token;
import com.example.safarity.model.Usuario;
import com.example.safarity.repository.ITicketRepository;
import com.example.safarity.repository.ITokenRepository;
import com.example.safarity.repository.IUsuarioRepository;
import com.example.safarity.security.auth.AuthDTO;
import com.example.safarity.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@RequiredArgsConstructor
public class UsuarioService {


    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findTopByAlias(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }


    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findTopByAlias(username).orElse(null);
    }

    public Usuario save(UsuarioDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return usuarioRepository.save(usuarioMapper.toEntity(dto));
    }


    //Listar
    public List<UsuarioDTO> listar() {
        return usuarioMapper.toDTO(usuarioRepository.findAll());
    }

    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        Usuario usuarioGuardar = usuarioMapper.toEntity(usuarioDTO);
        Usuario usuarioGuardada = usuarioRepository.save(usuarioGuardar);
        UsuarioDTO usuarioGuardadaDTO = usuarioMapper.toDTO(usuarioGuardada);
        return usuarioGuardadaDTO;
    }

    public Usuario getById(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    //public void eliminar(Integer id){usuarioRepository.deleteById(id);}

    public Usuario modificarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId()).orElse(null);

        if (usuario == null) {
            return null;
        } else {
            usuario.setAlias(usuarioDTO.getAlias());
            usuario.setPassword(usuarioDTO.getPassword());

            Usuario usuarioModificado = usuarioRepository.save(usuario);
            return usuarioModificado;

        }
    }


    public boolean validarPassword(Usuario usuario, String password) {

        return passwordEncoder.matches(password, usuario.getPassword());
    }


    public String getUserRol(String username) {
        Usuario loggedInUser = buscarPorUsername(username);
        if (loggedInUser != null) {
            return loggedInUser.getRol().toString();
        }
        return null;
    }

    public String getUserAlias(String alias) {
        Usuario loggedInUser = buscarPorUsername(alias);
        if (loggedInUser != null) {
            return loggedInUser.getAlias().toString();
        }
        return null;
    }


    private Map<String, Integer> usuariosYRoles = new HashMap<>();

    public UsuarioService() {
        // Inicialización de usuarios y roles (esto podría obtenerse de una base de datos)
        usuariosYRoles.put("ADMIN", 0);
        usuariosYRoles.put("ORGANIZACION", 1);
        usuariosYRoles.put("PARTICIPANTE", 2);
    }

    public Integer obtenerRolDelUsuario(String nombreUsuario) {
        // Simulación de obtener el rol del usuario desde la base de datos
        return usuariosYRoles.get(nombreUsuario);
    }

    public UsuarioDTO mostrarUsuario(String alias){
        Usuario usuario = usuarioRepository.findAllByAliasAndActivoTrue(alias);

        return usuarioMapper.toDTO(usuario);
    }

}
