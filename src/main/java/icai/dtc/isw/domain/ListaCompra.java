package icai.dtc.isw.domain;

import java.util.ArrayList;

public class ListaCompra {

    private ArrayList<EntryLista> listaCompra;

    public ListaCompra(){}

    public void insertarEntry(EntryLista entry){
        this.listaCompra.add(entry);
    }

    public ArrayList<String> verEntradas(){
        ArrayList<String> cadena = new ArrayList<>();
        cadena.add("Lista de la compra:");
        for(EntryLista entry : this.listaCompra){
            cadena.add(entry.entradaString());
        }
        cadena.add("Precio total: "+ calculaPrecio()+ "$");
        return cadena;
    }

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
}
