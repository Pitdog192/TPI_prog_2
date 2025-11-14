package entities;

public abstract class Base {
    private int id;
    private boolean eliminado;
    
    public Base(){
    }
    
    public Base(int id, boolean eliminado){
        this.id = id;
        this.eliminado = eliminado;
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isEliminado() {
        return eliminado;
    }
    
    public void setEliminado(boolean eliminado) {
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



