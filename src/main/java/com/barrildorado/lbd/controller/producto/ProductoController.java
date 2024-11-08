package com.barrildorado.lbd.controller.producto;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.barrildorado.lbd.dto.producto.DatosActualizarProducto;
import com.barrildorado.lbd.dto.producto.DatosListadoProducto;
import com.barrildorado.lbd.dto.producto.DatosRegistroProducto;
import com.barrildorado.lbd.dto.producto.DatosRespuestaProducto;
import com.barrildorado.lbd.service.producto.ProductoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/registrar")
    public ResponseEntity<DatosRespuestaProducto> registrarProducto(
            @Valid @RequestBody DatosRegistroProducto datosRegistroProducto,
            UriComponentsBuilder uriComponentsBuilder) {
        DatosRespuestaProducto datosRespuestaProducto = productoService.createProducto(datosRegistroProducto);
        URI url = uriComponentsBuilder.path("/buscar/{id_producto}")
                .buildAndExpand(datosRespuestaProducto.id_producto())
                .toUri();
        return ResponseEntity.created(url).body(datosRespuestaProducto);
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<DatosListadoProducto>> listarProductos(
            @PageableDefault( direction = Sort.Direction.ASC) Pageable paginacion) {
        Page<DatosListadoProducto> productoPage = productoService.getAllProductos(paginacion);
        return ResponseEntity.ok(productoPage);
    }

    @PutMapping("/actualizar")
    @Transactional
    public ResponseEntity<DatosRespuestaProducto> actualizarProducto(
            @Valid @RequestBody DatosActualizarProducto datosActualizarProducto) {
        DatosRespuestaProducto datosRespuestaProducto = productoService.updateProducto(datosActualizarProducto);
        return ResponseEntity.ok(datosRespuestaProducto);
    }

    @GetMapping("/buscar/{id_producto}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id_producto) {
        try {
            DatosRespuestaProducto producto = productoService.getProductoById(id_producto);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            String mensajeError = "Error al obtener el Producto con ID" + id_producto;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensajeError);
        }
    }

    @DeleteMapping("/eliminar/{id_producto}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id_producto) {
        try {
            productoService.deleteProducto(id_producto);
            return ResponseEntity.ok("Eliminacion Exitosa");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
