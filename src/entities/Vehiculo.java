package entities;

public class Vehiculo extends Base {

    private String dominio;
    private String marca;
    private String modelo;
    private Integer anio;
    private String nro_chasis;
    private Integer id_seguro;        // puede ser null en la BD
    private SeguroVehicular seguro;   // detalle (B)

    // Constructor vacío
    public Vehiculo() {
        super();
    }

    // Constructor solo con id y eliminado
    public Vehiculo(Long id, Boolean eliminado) {
        super(id, eliminado);
    }

    // Constructor sin objeto Seguro (solo FK)
    public Vehiculo(Long id, Boolean eliminado,
                    String dominio, String marca, String modelo,
                    Integer anio, String nro_chasis, Integer id_seguro) {
        super(id, eliminado);
        this.dominio = dominio;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.nro_chasis = nro_chasis;
        this.id_seguro = id_seguro;
    }

    // Constructor completo incluyendo el detalle SeguroVehicular
    public Vehiculo(Long id, Boolean eliminado,
                    String dominio, String marca, String modelo,
                    Integer anio, String nro_chasis,
                    Integer id_seguro, SeguroVehicular seguro) {
        super(id, eliminado);
        this.dominio = dominio;
        this.marca = marca;
        this.modelo = modelo;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
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

    // Para cumplir con la consigna A contiene private B detalle;
    public SeguroVehicular getDetalle() {
        return seguro;
    }

    public void setDetalle(SeguroVehicular seguro) {
        this.seguro = seguro;
    }

    public SeguroVehicular getSeguro() {
    return seguro;
    }

    public void setSeguro(SeguroVehicular seguro) {
    this.seguro = seguro;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + getId() +
                ", eliminado=" + getEliminado() +
                ", dominio='" + dominio + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio=" + anio +
                ", nro_chasis='" + nro_chasis + '\'' +
                ", id_seguro=" + id_seguro +
                '}';
    }
}