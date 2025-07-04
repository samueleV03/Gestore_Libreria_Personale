package decorator;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroAutoreDecorator extends Decorator {

    private String autore;
    public FiltroAutoreDecorator(FiltroDecorator filtro, String autore)
    {
        super(filtro);
        this.autore = autore;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri)
    {
        List<Libro> precedenti = super.filtra(libri); //dobbiamo applicare i filtri precedenti
        List<Libro> filtro = filtraPerAutore(precedenti);
        return filtro;
    }

    private List<Libro> filtraPerAutore(List<Libro> libri)
    {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getAutore().equalsIgnoreCase(autore)) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
