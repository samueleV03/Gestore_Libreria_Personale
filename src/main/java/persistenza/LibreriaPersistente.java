package persistenza;

import builder.Libro;

import java.io.IOException;
import java.util.List;

public interface LibreriaPersistente {
    //salva un singolo libro
    void salvaLibro(Libro libro);
    //rimuovi un singoli libro
    void rimuoviLibro(Libro libro);
    //aggiungi più libri contemporaneamente
    void salvaLibri(List<Libro> libri);
    //ottieni i libri che sono salvati
    List<Libro> ottieniLibri();
}
