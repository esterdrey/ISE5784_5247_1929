package primitives;

public class Material {

    public Double3 KD =Double3.ZERO, KS =Double3.ZERO;
    public int nShininess=0;

    public Material setKD(Double3 KD) {
        this.KD = KD;
        return this;
    }

    public Material setKS(Double3 KS) {
        this.KS = KS;
        return this;
    }

    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    public Material setKD(double kD) {
        this.KD =new Double3(kD) ;
        return this;
    }

    public Material setKS(double kS) {
        this.KS =new Double3(kS) ;
        return this;
    }

}
