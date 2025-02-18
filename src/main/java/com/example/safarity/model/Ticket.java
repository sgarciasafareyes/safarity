package com.example.safarity.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@Table(name = "ticket", schema = "safarity", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dinero_aportado")
    private Double dineroAportado;

    @Column(name = "fecha_compra")
    private LocalDate fecha;

    @Column(name = "activo")
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_participante", nullable = false)
    private Participante participante;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @OneToOne(cascade = CascadeType.PERSIST,mappedBy = "ticket", fetch = FetchType.LAZY)
    private Asistente asistente;


}
