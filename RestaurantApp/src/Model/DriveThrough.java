/**
 * DriveThrough.java
 *
 * Inherits from Order class, this is an order class with a priority level of 1
 *
 * @author Daniel Edinger
 */

package Model;


public class DriveThrough extends Order {

    public DriveThrough( String name, int orderNumber) {

        super(name, orderNumber, 1 );
    }
}
