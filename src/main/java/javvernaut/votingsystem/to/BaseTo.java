package javvernaut.votingsystem.to;


import javvernaut.votingsystem.HasId;

public abstract class BaseTo implements HasId {

    public BaseTo() {
    }

    public BaseTo(Integer id) {
        this.id = id;
    }

    protected Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
