package utility;

/**
 * An exception thrown when an incorrect player type is given in property file
 */
public class InvalidPlayerException extends Exception{
    public InvalidPlayerException(String type){
        super(type);
    }
}
