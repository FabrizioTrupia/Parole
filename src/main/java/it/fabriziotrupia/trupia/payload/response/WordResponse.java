package it.fabriziotrupia.trupia.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * La classe rappresenta la risposta contenente le informazioni associate a una parola.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class WordResponse {
    private String word;
    private boolean isPalindrome;
    private int totalCharacters;
    private int vowelCount;
    private int consonantCount;
    private Map<Character, Integer> letterFrequency;
}
