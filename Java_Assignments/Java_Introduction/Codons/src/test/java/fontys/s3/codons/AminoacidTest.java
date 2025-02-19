package fontys.s3.codons;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AminoacidTest {

    @Test
    void isPresent_shouldReturnFalse_whenSequenceIsNull() {
        Aminoacid aminoacid = new Aminoacid(Set.of(new Codon("ACG")));
        boolean actual = aminoacid.isPresent(null);
        assertFalse(actual);
    }

    @Test
    void isPresent_shouldReturnFalse_whenSequenceIsEmpty() {
        Aminoacid aminoacid = new Aminoacid(Set.of(new Codon("ACG")));
        boolean actual = aminoacid.isPresent(Collections.emptyList());
        assertFalse(actual);
    }

    @Test
    void isPresent_shouldReturnFalse_whenSequenceDoesNotContainAminoacid() {
        Aminoacid aminoacid = new Aminoacid(Set.of(new Codon("GGG")));
        List<Codon> sequence = new ArrayList<>();
        sequence.add(new Codon("ACG"));
        sequence.add(new Codon("AAA"));
        boolean actual = aminoacid.isPresent(sequence);
        assertFalse(actual);

    }

    @Test
    void isPresent_shouldReturnTrue_whenSequenceDoesNotContainAminoacid() {
        Set<Codon> possibleCodons = new HashSet<>();
        possibleCodons.add(new Codon("ATG"));
        Aminoacid aminoacid = new Aminoacid(possibleCodons);
        List<Codon> sequence = List.of(new Codon("CCC"), new Codon("ATG"));
        boolean actual = aminoacid.isPresent(sequence);
        assertTrue(actual);
    }

    @Test
    void getPossibleCodons_shouldReturnEmpty_whenPossibleCodonsIsNull() {
        Aminoacid aminoacid = new Aminoacid(null);
        Set<Codon> possibleCodons = aminoacid.getPossibleCodons();
        assertNotNull(possibleCodons);
        assertEquals(0, possibleCodons.size());
    }

    @Test
    void getPossibleCodons_shouldReturnUnmodifiableSet() {
        Aminoacid aminoacid = new Aminoacid(Set.of(new Codon("AAA")));
        Set<Codon> possibleCodons = aminoacid.getPossibleCodons();
        assertThrows(UnsupportedOperationException.class,
                () -> possibleCodons.add(new Codon("GGG")));
    }
}