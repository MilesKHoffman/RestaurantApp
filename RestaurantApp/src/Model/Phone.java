/**
 * Phone.java
 *
 * Inherits from Order class, this is an order class with a priority level of 3
 *
 * @author  Daniel Edinger
 */

package Model;

public class Phone extends Order{
    private String phoneNumber;
    public Phone(String name, int orderNumber, String phone ) {
        super(name,orderNumber, 3);
        phoneNumber = phone;
    }

    /**
     * Gets the phone number
     * @return the phone number
     */
    public int getPhoneNumber() {
        if (getOrderType()==3){

            return Integer.parseInt(phoneNumber);
        }
        else{return 0;}
    }
}
