package entities;
import java.util.Date;

public class Vehiculo extends Base{
    private String dominio;
    private String marca;
    private Integer anio;
    private String nro_chasis;
    private int id_seguro;
    private SeguroVehicular seguro;

    // Constructor vacío
    public Vehiculo() {
        super();
    }
    
    // Constructor solo con id y eliminado
    public Vehiculo(int id, boolean eliminado) {
        super(id, eliminado);
    }
    
    // Constructor evitando seguro
    public Vehiculo(int id, boolean eliminado, String dominio, String marca, Integer anio, String nro_chasis, int id_seguro) {
        super(id, eliminado);
        this.dominio = dominio;
        this.marca = marca;
        this.anio = anio;
        this.nro_chasis = nro_chasis;
        this.id_seguro = id_seguro;
    }
    
    // Constructor completo incluyendo el detalle (SeguroVehicular)
    public Vehiculo(int id, boolean eliminado, String dominio, String marca, String modelo, Integer anio, String nro_chasis, Integer id_seguro, SeguroVehicular seguro) {
        super(id, eliminado);
        this.dominio = dominio;
        this.marca = marca;
        this.marca = marca;
        this.anio = anio;
        this.nro_chasis = nro_chasis;
        this.id_seguro = id_seguro;
        this.seguro = seguro;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getNro_chasis() {
        return nro_chasis;
    }

    public void setNro_chasis(String nro_chasis) {
        this.nro_chasis = nro_chasis;
    }

    public Integer getId_seguro() {
        return id_seguro;
    }

    public void setId_seguro(Integer id_seguro) {
        this.id_seguro = id_seguro;
    }

    public SeguroVehicular getDetalle() {
        return seguro;
    }

    public void setDetalle(SeguroVehicular seguro) {
        this.seguro = seguro;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + getId() +
                ", eliminado=" + isEliminado() +
                ", dominio='" + dominio + '\'' +
                ", marca='" + marca + '\'' +
                ", anio=" + anio +
                ", nro_chasis='" + nro_chasis + '\'' +
                ", id_seguro=" + id_seguro +
                ", detalle=" + (seguro != null ? seguro.toString() : "sin seguro") +
                '}';
    }
}
