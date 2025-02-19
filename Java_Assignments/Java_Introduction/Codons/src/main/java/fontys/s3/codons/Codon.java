package fontys.s3.codons;

import java.util.Objects;

public class Codon {
    private final Nucleotide first;
    private final Nucleotide second;
    private final Nucleotide third;

    public Codon(Nucleotide first, Nucleotide second, Nucleotide third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Codon(char first, char second, char third) {
        this(
                Nucleotide.valueOf(String.valueOf(first)),
                Nucleotide.valueOf(String.valueOf(second)),
                Nucleotide.valueOf(String.valueOf(third)));
    }

    public Codon(String nucleotides) {
        this(nucleotides.charAt(0), nucleotides.charAt(1), nucleotides.charAt(2));
    }

    @Override
    public String toString() {
        return "" + first + second + third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Codon codon = (Codon) o;
        return Objects.equals(first, codon.first)
                && Objects.equals(second, codon.second)
                && Objects.equals(third, codon.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }


    public Nucleotide getFirst() {
        return first;
    }
    public Nucleotide getSecond() {
        return second;
    }
    public Nucleotide getThird() {
        return third;
    }
}

