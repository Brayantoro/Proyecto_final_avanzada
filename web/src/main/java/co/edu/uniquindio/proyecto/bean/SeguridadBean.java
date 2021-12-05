package co.edu.uniquindio.proyecto.bean;


import co.edu.uniquindio.proyecto.dto.ProductoCarrito;
import co.edu.uniquindio.proyecto.entidades.*;
import co.edu.uniquindio.proyecto.servicios.ProductoServicio;
import co.edu.uniquindio.proyecto.servicios.UsuarioServicio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Scope("session")
@Component
public class SeguridadBean implements Serializable {

    Logger logger = Logger.getLogger(SeguridadBean.class.getName());

    @Getter @Setter
    private boolean autenticado;

    @Getter @Setter
    private String email,password;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Getter @Setter
    Usuario usuarioSesion;

    @Getter @Setter
    private ArrayList<ProductoCarrito> productosCarrito;

    @Getter @Setter
    private ArrayList<Producto> productosFavoritos;

    @Getter @Setter
    private List<Compra> comprasUsuario;

    @Getter @Setter
    private List<DetalleCompra> detalleCompras;

    @Getter @Setter
    private Producto productoActual;

    @Getter @Setter
    private Float subtotal ;


    @Getter @Setter
    private String medioPago;

    @Getter @Setter
    private Subasta subasta;



    @Autowired
    private ProductoServicio productoServicio;



    @PostConstruct
    public void inicializar(){
        this.subtotal=0F;
        this.productosCarrito = new ArrayList<>();
        this.productosFavoritos = new ArrayList<>();
        this.medioPago = new String();
        subasta = new Subasta();


    }


    public String iniciarSesion(){
        if(!email.isEmpty() && !password.isEmpty()){
            try {
                usuarioSesion= usuarioServicio.iniciarSesion(email,password);
                autenticado=true;
                return "/index?faces-redirect=true";
            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Alerta",e.getMessage());
                FacesContext.getCurrentInstance().addMessage("login-bean",fm);
            }
        }
        return null;
    }

    public String cerrarSesion(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index?faces-redirect=true";
    }

    public void agragarAlCarrito(Integer codigo,Integer precio,String nombre,String imagen){

        ProductoCarrito pc = new ProductoCarrito(codigo,nombre,imagen,precio,1);
        if(!productosCarrito.contains(pc)) {
            productosCarrito.add(pc);
            subtotal+= precio;
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,"Alerta","El producto se agrago al carrito");
            FacesContext.getCurrentInstance().addMessage("add-cart",fm);
        }
    }



    public void eliminarDelCarrito(int indice){
        subtotal -= productosCarrito.get(indice).getPrecio();
        productosCarrito.remove(indice);

    }

    public void actualizarSubtotal(){
        subtotal=0f;
        for(ProductoCarrito p : productosCarrito){
            subtotal+= p.getPrecio()*p.getUnidades();
        }
    }

    public void comprar(){
        if(usuarioSesion!=null && !productosCarrito.isEmpty()){

            try {
                productoServicio.comprarProductos(usuarioSesion,productosCarrito,medioPago) ;
                productosCarrito.clear();
                subtotal=0f;
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,"Alerta","Compra Realizada");
                FacesContext.getCurrentInstance().addMessage("compra-msj",fm);
            } catch (Exception e) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Alerta",e.getMessage());
                FacesContext.getCurrentInstance().addMessage("compra-msj",fm);
            }
        }

    }

    public Integer unidadesProducto(Integer codigo){

        try {
            return productoServicio.obtenerUnidadesProducto(codigo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void eliminarProducto(Integer codigo){

        try {
            productoServicio.eliminarProducto(codigo);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,"Alerta","El Producto se elimino correctamente");
            FacesContext.getCurrentInstance().addMessage("eliminar-msj",fm);
        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Alerta",e.getMessage());
            FacesContext.getCurrentInstance().addMessage("eliminar-msj",fm);
        }

    }



}
