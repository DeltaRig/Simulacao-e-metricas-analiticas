public class RNG {
    private long a;
    private long c;
    private long m;
    private long x;

    // Parameters based on java implementation according to wikipedia.
    public RNG(long seed) {
        a = Long.parseUnsignedLong("25214903917");
        c = 11L;
        m = 1L << 48;
        x = seed;
    }
    
  
    public double next() {
        x = Long.remainderUnsigned(a*x+c , m);
        double resposta = x/(double)m;
        return resposta; // retorna um aleátorio entre 1 e 0
    }
}