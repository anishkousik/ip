package typecast.exception;

/**
 * Represents exceptions specific to the TypeCast application.
 * This exception is thrown when user commands are invalid or operations fail.
 */
public class TypeCastException extends Exception {
    
    /**
     * Creates a new TypeCastException with the specified error message.
     *
     * @param message The detailed error message.
     */
    public TypeCastException(String message) {
        super(message);
    }
}