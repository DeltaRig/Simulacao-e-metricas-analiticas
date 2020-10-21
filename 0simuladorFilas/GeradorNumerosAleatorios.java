public class GeradorNumerosAleatorios {
    private long a;
    private long c;
    private long m;
    private long x;


    public GeradorNumerosAleatorios(long semente) {
        a = Long.parseUnsignedLong("3089823433");
        c = 11L;
        m = Integer.MAX_VALUE;
        x = semente;
    }
    
    //correção feita (usar um método next)
    public double next() {
        x = Long.remainderUnsigned(a*x+c , m);
        double resposta = x/(double)m;
        return resposta; // retorna um aleátorio entre 1 e 0
    }
}