package com.example.safarity.dto;

import com.example.safarity.model.enums.TipoEvento;
import com.example.safarity.model.enums.TipoPago;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class EventoDTO {
    private Long id;

    private String nombre;

    private String direccion;

    private String descripcion;

    private String imagen;

    private Integer aforo;

    private Integer totalAsistentes;

    private String fecha_lanzamiento;

    private String fecha_venta;

    private String fecha_inicio;

    private String fecha_fin;

    private Integer entradasVendidas;

    private TipoEvento tipoEvento;

    private TipoPago tipoPago;

    private EventoDTO eventoDTO;
}
