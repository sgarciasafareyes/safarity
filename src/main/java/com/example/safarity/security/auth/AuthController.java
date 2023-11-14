package com.example.safarity.security.auth;

import com.example.safarity.controller.ParticipanteController;
import com.example.safarity.dto.OrganizacionDTO;
import com.example.safarity.dto.ParticipanteDTO;
import com.example.safarity.dto.UsuarioDTO;
import com.example.safarity.model.Organizacion;
import com.example.safarity.model.Participante;
import com.example.safarity.model.Token;
import com.example.safarity.model.Usuario;
import com.example.safarity.model.enums.Rol;
import com.example.safarity.repository.IEventoRepository;
import com.example.safarity.repository.IOrganizacionRepository;
import com.example.safarity.repository.IParticipanteRepository;
import com.example.safarity.repository.IUsuarioRepository;
import com.example.safarity.security.jwt.JWTService;
import com.example.safarity.service.OrganizacionService;
import com.example.safarity.service.ParticipanteService;
import com.example.safarity.service.TokenService;
import com.example.safarity.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ParticipanteService participanteService;

    @Autowired
    private IParticipanteRepository iParticipanteRepository;

    @Autowired
    private OrganizacionService organizacionService;

    @Autowired
    private IOrganizacionRepository iOrganizacionRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;



    @PostMapping("/login")
    public AuthDTO login(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = (Usuario) usuarioService.loadUserByUsername(usuarioDTO.getAlias());
        String apiKey = null;
        String mensaje;

        if (usuario != null) {
            if (usuarioService.validarPassword(usuario, usuarioDTO.getPassword())) {

                mensaje = "Usuario Logueado";

                //Usuario sin token
                if (usuario.getToken() == null) {
                    apiKey = jwtService.generateToken(usuario);
                    Token token = new Token();
                    token.setUsuario(usuario);
                    token.setToken(apiKey);
                    token.setFechaExpiracion(LocalDateTime.now().plusDays(1));
                    tokenService.save(token);

                    //Usuario con token caducado
                } else if (usuario.getToken().getFechaExpiracion().isBefore(LocalDateTime.now())) {
                    Token token = usuario.getToken();
                    apiKey = jwtService.generateToken(usuario);
                    token.setToken(apiKey);
                    token.setFechaExpiracion(LocalDateTime.now().plusDays(1));
                    tokenService.save(token);

                    //Usuario con token válido
                } else {
                    apiKey = usuario.getToken().getToken();
                }
            } else {
                mensaje = "Contraseña no válida";
            }
        } else {
            mensaje = "Usuario No encontrado";
        }

        return AuthDTO
                .builder()
                .token(apiKey)
                .info(mensaje)
                .build();
    }

    @PostMapping("/register")
    public AuthDTO register(@RequestBody ParticipanteDTO participanteDTO){
        for (Participante p : iParticipanteRepository.findAll()){
            if (p.getDni().equals(participanteDTO.getDni()) || p.getUsuario().getAlias().equals(participanteDTO.getUsuarioDTO().getAlias())){
                return AuthDTO.builder().info("Ya existe").build();
            }else {
                participanteDTO.getUsuarioDTO().setRol(Rol.PARTICIPANTE);
                Participante participanteNuevo = participanteService.save(participanteDTO);
                String token = jwtService.generateToken(participanteNuevo.getUsuario());

                return AuthDTO
                        .builder()
                        .token(token)
                        .info("Usuario creado correctamente")
                        .build();
            }
        }
        return null;
    }

    @PostMapping("/registerOrganizacion")
    public AuthDTO registerOrganizacion(@RequestBody OrganizacionDTO organizacionDTO){
        if (iOrganizacionRepository.findTopByCif(organizacionDTO.getCif()) != null || usuarioRepository.findTopByAlias(organizacionDTO.getUsuarioDTO().getAlias()) != null) {
            return AuthDTO.builder().info("Ya existe").build();
        } else {
            organizacionDTO.getUsuarioDTO().setRol(Rol.ORGANIZACION);
            Organizacion organizacionNueva = organizacionService.save(organizacionDTO);
            String token = jwtService.generateToken(organizacionNueva.getUsuario());

            return AuthDTO
                    .builder()
                    .token(token)
                    .info("Usuario creado correctamente")
                    .build();
        }

    }


}