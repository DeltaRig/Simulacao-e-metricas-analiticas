public class GeradorNumerosAleatorios {
    private long a;
    private long c;
    private double m;
    private long x;

    // Consultamos https://aaronschlegel.me/linear-congruential-generator-r.html
    public GeradorNumerosAleatorios(long semente) {
        x = semente;
        
        m = Math.pow(2, 32);
        a = Long.parseUnsignedLong("1103515245");
        c = 12345L;
    }
    
    //correção feita (usar um método next)
    public double next() {
        x = Long.remainderUnsigned(a * x + c , (long) m);
        double resposta = x / (double)m;
        return resposta; // retorna um aleátorio entre 1 e 0
    }
}