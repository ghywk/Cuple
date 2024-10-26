package bh.dpl.value;

public abstract class Value<V> {
    public V value;
    public String name;
    protected final Dependency dependency;

    public Value(final String name, final Dependency dependenc) {
        this.name = name;
        this.dependency = dependenc;
    }

    public Value(final String name) {
        this.name = name;
        this.dependency = (() -> Boolean.TRUE);
    }

    public abstract <T> T getConfigValue();

    public boolean isAvailable() {
        return this.dependency != null && this.dependency.check();
    }

    public V getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @FunctionalInterface
    public interface Dependency
    {
        boolean check();
    }
}
