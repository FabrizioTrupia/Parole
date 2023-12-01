package it.fabriziotrupia.trupia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * La classe rappresenta l'entità per le parole.
 * Ho usato l'annotazione @Entity per indicare che è una classe persistente e mappata a una tabella nel database.
 * Ho usato lombok per generare automaticamente getter, setter, costruttori vuoti e il metodo toString.
 */
@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Word {
    /**
     * La varibile id è annotato con @Id per impostarla come chiave primaria. Inerendo strategy su IDENTITY,
     * mi permette di generato automaticamente il valore dell'id all'atto della creazione di una nuova parola.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * La variabile word rappresenta di per se la parola creata.
     * con l'annotazione colum specifico che deve essere massimo lunga 100 caratteri, non può essere nulla e deve essere univoca.
     */
    @Column(length = 100 , nullable = false , unique = true)
    private String word;

    /**
     * Metodo costruttore della classe Word che ha come parametro la stringa word
     */
    public Word(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return id == word.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
