package entities;

import java.util.Date;

public class SeguroVehicular extends Base {
    private String aseguradora;
    private String nro_poliza;
    private Cobertura cobertura;
    private Date vencimiento;

    // Constructor vacío
    public SeguroVehicular() {
        super();
    }

    // Constructor solo con id y eliminado
    public SeguroVehicular(Long id, Boolean eliminado) {
        super(id, eliminado);
    }

    // Constructor completo (incluyendo campos de Base)
    public SeguroVehicular(Long id, Boolean eliminado,
                           String aseguradora, String nro_poliza,
                           Cobertura cobertura, Date vencimiento) {
        super(id, eliminado);
        this.aseguradora = aseguradora;
        this.nro_poliza = nro_poliza;
        this.cobertura = cobertura;
        this.vencimiento = vencimiento;
    }

    public String getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(String aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getNro_poliza() {
        return nro_poliza;
    }

    public void setNro_poliza(String nro_poliza) {
        this.nro_poliza = nro_poliza;
    }

    public Cobertura getCobertura() {
        return cobertura;
    }

    public void setCobertura(Cobertura cobertura) {
        this.cobertura = cobertura;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    @Override
    public String toString() {
        return "SeguroVehicular{" +
                "id=" + getId() +
                ", eliminado=" + getEliminado() +
                ", aseguradora='" + aseguradora + '\'' +
                ", nro_poliza='" + nro_poliza + '\'' +
                ", cobertura='" + cobertura + '\'' +
                ", vencimiento=" + vencimiento +
                '}';
    }
}