package persistenza;

import builder.Libro;

import java.util.List;

public interface LibreriaPersistente {
    //salva un singolo libro
    boolean salvaLibro(Libro libro);
    //rimuovi un singoli libro
    boolean rimuoviLibro(Libro libro);
    //aggiungi più libri contemporaneamente
    void salvaLibri(List<Libro> libri);
    //ottieni i libri che sono salvati
    List<Libro> ottieniLibri();

    Libro trovaLibro(String isbn);
}
