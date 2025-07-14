package persistenza;

import builder.Libro;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public enum PersistenzaImpl implements LibreriaPersistente{

    INSTANCE; //implementanzione del singleton con enum, abbiamo visto essere migliore

    private static final String FILE_PATH = "libreria.json";
    private final Gson gson;
    private List<Libro> libriNellaLibreria;

    PersistenzaImpl() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        //GsonBuilder serve per creare il Gson personalizzato, se ci va bene quello standard new Gson()
        //setPrettyPrinting serve per attivare il formato leggibile su più righe, create serve per crearlo
        this.libriNellaLibreria = new ArrayList<>();
        try {this.libriNellaLibreria = ottieniLibri();}
        catch (Exception e) {this.libriNellaLibreria = new ArrayList<>();}
    }

    //metodo utile per il test
    public synchronized void reset() {
        libriNellaLibreria.clear();
        try {
            Files.deleteIfExists(Paths.get(FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized  boolean salvaLibro(Libro libro){
        if (!libriNellaLibreria.contains(libro)) {
            libriNellaLibreria.add(libro);
            salvaLibri(libriNellaLibreria);
            return true;
        } else {
            System.out.println("Libro già presente");
            return false;
        }
    }

    @Override
    public synchronized boolean rimuoviLibro(Libro libro){
        if (libriNellaLibreria.contains(libro)) {
            libriNellaLibreria.remove(libro);
            salvaLibri(libriNellaLibreria);
            return true;
        } else {
            System.out.println("Il libro non è presente");
            return false;
        }

    }

    @Override
    public synchronized void salvaLibri(List<Libro> libri){
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(libri, writer);
            libriNellaLibreria = new ArrayList<>(libri); //aggiorniamo la lista dei libri disponibili
            //questo approccio va bene perché il metodo viene usato solo in salva libro e rimuovi libro che ogni volta
            //quando andranno a chiamare il metodo salvaLibri gli passeranno in input libriNellaLibreria
        } catch (IOException e) {
            System.out.println("Problema nel salvataggio");
        }
    }

    @Override
    public synchronized List<Libro> ottieniLibri(){
        if (!Files.exists(Paths.get(FILE_PATH))) {
            return new ArrayList<>(); //se il file non esiste non abbiamo libri salvati, allora restituiamo una lista vuota
        } //altrimenti restituiamo il contenuto del file
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Libro>>() {}.getType();
            List<Libro> libri = gson.fromJson(reader, listType); //leggiamo il contenuto del file e convertiamolo in una lista di libri
            return libri;
        } catch (IOException e) {
            System.out.println("Problema nel recuperare libri");
        }
        return new ArrayList<>(); //nel caso ci siano problemi facciamo restituire lista vuota
    }

    @Override
    public Libro trovaLibro(String isbn){
        Libro ret=null;
        for (Libro libro : libriNellaLibreria) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        throw new LibroNonTrovatoException(isbn);
    }
}
