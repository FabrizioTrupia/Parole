package it.fabriziotrupia.trupia.repository;

import it.fabriziotrupia.trupia.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * L'interfaccia estende JpaRepository per la gestione delle operazioni sulle parole nel database.
 */
public interface WordRepository extends JpaRepository<Word, Long> {

    /**
     * Verifica se una parola è presente nel database.
     */
    boolean existsByWord(String word);
}
