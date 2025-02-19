package fontys.s3.codons;

import java.util.*;

public class AminoacidCounter {

    public Map<Aminoacid, Integer> countAminoacids(List<Codon> sequence, Set<Aminoacid> aminoacids) {
        Map<Aminoacid, Integer> countPerAminoacid = new HashMap<>();

        for (Aminoacid aminoacid : aminoacids) {
            countPerAminoacid.put(aminoacid, 0);
        }

        for (Codon codon : sequence) {
            for (Aminoacid aminoacid : aminoacids) {
                if (aminoacid.getCodons().contains(codon)) {
                    countPerAminoacid.put(aminoacid, countPerAminoacid.get(aminoacid) + 1);
                }
            }
        }

        return countPerAminoacid;
    }
}
