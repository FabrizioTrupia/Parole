package it.fabriziotrupia.trupia.payload.request;

import lombok.Getter;

/**
 * La classe rappresenta la richiesta per calcolare lo score di similarità tra due parole.
 */
@Getter
public class RequestScore {
    private long firstId;
    private long secondId;
}
