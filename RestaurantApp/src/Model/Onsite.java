/**
 * Onsite.java
 *
 * Inherits from Order class, this is an order class with a priority level of 2
 *
 * @author Miles Hoffman
 */

package Model;


public class Onsite extends Order{
    public Onsite(String name, int orderNumber) {
        super(name, orderNumber, 2);
    }
}
