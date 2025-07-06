package persistenza;

public class LibroNonTrovatoException extends RuntimeException{

    public LibroNonTrovatoException(String isbn)
    {
        super("Libro non trovato: " + isbn);
    }
}
