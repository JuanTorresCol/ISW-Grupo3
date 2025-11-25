package icai.dtc.isw.domain;

public class Ingrediente {

    private final String nombre;
    private final String cantidad;

    // constructor del ingrediente en función a sus atributos
    public Ingrediente(String nombre, String cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {return this.nombre;}

    public String getCantidad() {return this.cantidad;}

    @Override
    public String toString()
    {
        return getNombre() + " --> " + getCantidad();
    }

    // devuelve el valor numerico de la cantidad
    public int getCantidadNum(){
        return Integer.parseInt(cantidad.replace("kg", "").
                replace("g", "").replace("ml","").
                replace("l", "").replace("u", ""));
    }

}
