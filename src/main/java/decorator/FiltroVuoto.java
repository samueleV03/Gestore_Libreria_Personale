package decorator;


import builder.Libro;

import java.util.List;

public class FiltroVuoto implements FiltroDecorator {

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        return libri; //non facciamo nessun filtro
    }
}
