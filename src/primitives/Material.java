package primitives;

public class Material {

    public Double3 KD = Double3.ZERO, KS = Double3.ZERO, KT = Double3.ZERO, KR = Double3.ZERO;
    public int nShininess = 0;

    /**
     * Sets the diffuse reflection coefficient
     * @param KD the diffuse reflection coefficient
     * @return the current Material object
     */
    public Material setKD(Double3 KD) {
        this.KD = KD;
        return this;
    }

    /**
     * Sets the specular reflection coefficient
     * @param KS the specular reflection coefficient
     * @return the current Material object
     */
    public Material setKS(Double3 KS) {
        this.KS = KS;
        return this;
    }

    /**
     * Sets the shininess factor
     * @param nShininess the shininess factor
     * @return the current Material object
     */
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient using a double value
     * @param kD the diffuse reflection coefficient
     * @return the current Material object
     */
    public Material setKD(double kD) {
        this.KD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient using a double value
     * @param kS the specular reflection coefficient
     * @return the current Material object
     */
    public Material setKS(double kS) {
        this.KS = new Double3(kS);
        return this;
    }

    /**
     * Sets the transparency coefficient
     * @param KT the transparency coefficient
     * @return the current Material object
     */
    public Material setKT(Double3 KT) {
        this.KT = KT;
        return this;
    }

    /**
     * Sets the reflection coefficient
     * @param KR the reflection coefficient
     * @return the current Material object
     */
    public Material setKR(Double3 KR) {
        this.KR = KR;
        return this;
    }

    /**
     * Sets the transparency coefficient using a double value
     * @param KT the transparency coefficient
     * @return the current Material object
     */
    public Material setKT(double KT) {
        this.KT = new Double3(KT);
        return this;
    }

    /**
     * Sets the reflection coefficient using a double value
     * @param KR the reflection coefficient
     * @return the current Material object
     */
    public Material setKR(double KR) {
        this.KR = new Double3(KR);
        return this;
    }

    /**
     * Gets the diffuse reflection coefficient
     * @return the diffuse reflection coefficient
     */
    public Double3 getKD() {
        return KD;
    }

    /**
     * Gets the specular reflection coefficient
     * @return the specular reflection coefficient
     */
    public Double3 getKS() {
        return KS;
    }
}
