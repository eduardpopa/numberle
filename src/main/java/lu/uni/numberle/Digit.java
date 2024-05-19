package lu.uni.numberle;

public class Digit {
    private Integer value;
    private State state;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Digit() {
        this.state = State.UNKNOWN;
    }
}
