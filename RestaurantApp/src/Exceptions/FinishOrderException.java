package Exceptions;

public class FinishOrderException extends Exception{

    public FinishOrderException(){
        super();
        System.out.println( "There are no orders that can be finished" );
    }
}


