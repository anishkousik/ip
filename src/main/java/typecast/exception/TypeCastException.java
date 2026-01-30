package typecast.exception;

/**
 * Represents exceptions specific to the TypeCast application.
 * Used to signal errors in command parsing and task operations.
 */
public class TypeCastException extends Exception {

    /**
     * Constructs a TypeCastException with the specified error message.
     *
     * @param message The detailed error message.
     */
    public TypeCastException(String message) {
        super(message);
    }
}