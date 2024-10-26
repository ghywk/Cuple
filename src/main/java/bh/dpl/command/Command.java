package bh.dpl.command;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 12
 */
public abstract class Command {
    protected final String[] key;

    public Command(String[] key) {
        this.key = key;
    }

    public String[] getKey() {
        return key;
    }

    public abstract void runCommand(String[] args);
}
