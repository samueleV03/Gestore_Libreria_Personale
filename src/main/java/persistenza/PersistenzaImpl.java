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
    public synchronized  void salvaLibro(Libro libro){
        if (!libriNellaLibreria.contains(libro)) {
            libriNellaLibreria.add(libro);
            salvaLibri(libriNellaLibreria);
        } else {
            System.out.println("Libro già presente");
        }
    }

    @Override
    public synchronized void rimuoviLibro(Libro libro){
        if (libriNellaLibreria.contains(libro)) {
            libriNellaLibreria.remove(libro);
            salvaLibri(libriNellaLibreria);
        } else {
            System.out.println("Il libro non è presente");
        }

    }

    @Override
    public synchronized void salvaLibri(List<Libro> libri){
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(libri, writer);
            libriNellaLibreria = new ArrayList<>(libri); //aggiorniamo la lista dei libri disponibili
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
}
