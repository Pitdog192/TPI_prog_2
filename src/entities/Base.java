package entities;

/**
 *
 * @author Arroquigarays
 */
public abstract class Base {
    private Long id;
    private Boolean eliminado;

    public Base() {
    }

    public Base(Long id, Boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Base{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                '}';
    }
}