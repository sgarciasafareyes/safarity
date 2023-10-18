package com.example.safarity.model;

import com.example.safarity.model.enums.TipoEvento;
import com.example.safarity.model.enums.TipoPago;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name="evento", schema="safarity", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"organizacion", "participantes", "tickets"})
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nombre")
    private String nombre;

    @Column(name="direccion")
    private String direccion;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="imagen")
    private String imagen;

    @Column(name="aforo")
    private Integer aforo;

    @Column(name="total_asistentes")
    private Integer totalAsistentes;

    @Column(name="fecha_lanzamiento")
    private Date fechaLanzamiento;

    @Column(name="fecha_venta")
    private Date fechaVentaDisponible;

    @Column(name="fecha_inicio")
    private Date fechaInicio;

    @Column(name="fecha_fin")
    private Date fechaFin;

    @Column(name="entradas_vendidas")
    private Integer entradasVendidas;

    @Column(name = "activo")
    private boolean activo = true;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;


    @ManyToOne
    @JoinColumn(name = "id_organizacion")
    private Organizacion organizacion;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="evento_participante",
        joinColumns = {@JoinColumn(name = "id_evento", nullable=false)},
        inverseJoinColumns = {@JoinColumn(name= "id_participante", nullable=false)})
    private Set<Participante> participantes= new HashSet<>(0);

    @OneToMany(mappedBy = "evento" , fetch = FetchType.LAZY)
    private Set<Ticket> tickets;





}
