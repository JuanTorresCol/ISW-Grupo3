package icai.dtc.isw.domain;

import java.util.ArrayList;

public class ListaCompra implements Cloneable{

    private ArrayList<EntryLista> listaCompra;

    public ListaCompra(){
        this.listaCompra = new ArrayList<>();
    }

    public void insertarEntry(EntryLista entry){
        this.listaCompra.add(entry);
    }

    // devuelve las entradas de la lista formateadas correctamente
    public ArrayList<String> verEntradas(){
        ArrayList<String> cadena = new ArrayList<>();
        cadena.add("Lista de la compra:");
        for(EntryLista entry : this.listaCompra){
            cadena.add(entry.entradaString());
        }
        cadena.add("Precio total: "+ Math.round(calculaPrecio()*100)/100+ "$");
        return cadena;
    }

    // calcula el precio de la lista de la compra en su entero
    public double calculaPrecio(){
        double calc = 0;
        for(EntryLista entry : this.listaCompra){
            calc += entry.getPrecio();
        }
        return calc;
    }

    public ArrayList<EntryLista> getEntries(){
        return this.listaCompra;
    }

    @Override
    public ListaCompra clone() {
        try {
            ListaCompra copia = (ListaCompra) super.clone();
            copia.listaCompra = new ArrayList<>();
            for (EntryLista entry : this.listaCompra) {
                if (entry != null) {
                    try {
                        copia.listaCompra.add(entry.clone());
                    } catch (Exception e) {
                        copia.listaCompra.add(entry);
                    }
                } else {
                    copia.listaCompra.add(entry);
                }
            }

            return copia;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
