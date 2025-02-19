package fontys.s3.codons;

public enum Nucleotide {
    A, T, C, G;

    public static Nucleotide fromChar(char c) {
        switch (Character.toUpperCase(c)) {
            case 'A': return A;
            case 'T': return T;
            case 'C': return C;
            case 'G': return G;
            default: throw new IllegalArgumentException("Invalid nucleotide: " + c);
        }
    }
}
