/**
 * DoorDash.java
 *
 * Inherits from Order class, this is an order class with a priority level of 4
 *
 * @author Daniel Edinger
 */

package Model;

public class DoorDash extends Order {

    public DoorDash( String name, int orderNumber) {

        super( name, orderNumber, 4 );
    }
}
