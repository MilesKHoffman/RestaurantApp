package Controller;

import Model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileWriterController{
        public static ArrayList<Order> fileOrderArrayList = new ArrayList<>();
        //Creates a static array list to store orders in
        private static File file = new File("savedData.txt");
        //Creates a static file to save the orders in
        OrderData orderData = new OrderData();


    public static void closeFileAction(FileWriterController fileWriterController) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                //Creates a new file writer object
                fileWriterController.writeToFile();
                //Writes all orders in the system to the file
                fileWriter.close();
                //Closes file
            } catch (IOException e) {
                System.out.println("ERROR");
                throw new RuntimeException(e);
            }
        }

        private void writeToFile() throws IOException {
            /**
             * ORDER TYPE
             * CUSTOMER NAME
             * ORDER NUMBER
             * PHONE NUMBER (IF APPLICABLE)
             * [ItemID, ItemID, ItemID]
             * [Quantity, Quantity, Quantity]
             * TIMES SKIPPED
             * IS COMPLETED
             **/
            //Order storage template
            FileWriter fileWriter = new FileWriter(file);
            //Creates file writer to write orders to file
            try {
                for (int i=0;i<fileOrderArrayList.size();i++) {
                    //Loops through all orders in the array list
                    fileWriter.write(fileOrderArrayList.get(i).getOrderType() + String.format("%n"));
                    //Writes in order type and then ends the line
                    fileWriter.write(fileOrderArrayList.get(i).getName() + String.format("%n"));
                    //Writes in order name and then ends the line
                    fileWriter.write(fileOrderArrayList.get(i).getOrderNumber() + String.format("%n"));
                    //Writes in order number and then ends the line
                    if (fileOrderArrayList.get(i).getOrderType() == 3) {
                        Phone phone = (Phone) fileOrderArrayList.get(i);
                        fileWriter.write(((phone.getPhoneNumber() + String.format("%n"))));
                        //Writes in order phone number and then ends the line
                    }
                    fileWriter.write(String.valueOf(fileOrderArrayList.get(i).getItemID()) + String.format("%n"));
                    //Writes in order items as an array and then ends the line
                    fileWriter.write(String.valueOf(fileOrderArrayList.get(i).getQuantities()) + String.format("%n"));
                    //Writes in order quantities as an array and then ends the line
                    fileWriter.write(fileOrderArrayList.get(i).getIfSkipped() + String.format("%n"));
                    //Writes in order times skipped and then ends the line
                    fileWriter.write((fileOrderArrayList.get(i).getIsComplete() + String.format("%n")));
                    //Writes in order completion status and then ends the line
                }
            } catch (IOException e) {
                e.getStackTrace();
                //Catches exception
            }
            fileWriter.close();
            //Closes file
        }

        private List<Integer> toIntArray(String x) { //Converts the string line to int array
            x = x.replace("[", ""); //Removes initial bracket for the input string
            x = x.replace("]", ""); //Removes final bracket for the input string
            x = x.replace(" ", ""); //Removes all spaces from the input string
            String[] y = x.split(","); //Splits the string into a string array at commas
            List<Integer> array = new ArrayList<>();
            for (int i = 0; i < y.length; i++) {
                array.add(i, Integer.parseInt(y[i])); //Converts string array to integer list
            }
            return array;
        }

        public void readInFile() throws FileNotFoundException {
            Scanner s = new Scanner(file); //Read in from file
            while (s.hasNextLine()) {
                //Loops while there are still orders in file
                Order order;
                //Creates order object
                int type = Integer.parseInt(s.nextLine());
                //Stores order type in variable
                if (type == 4) {
                    order = new DoorDash(s.nextLine(), Integer.parseInt(s.nextLine()));
                    //Makes order a DoorDash order
                } else if (type == 1) {
                    order = new DriveThrough(s.nextLine(), Integer.parseInt(s.nextLine()));
                    //Makes order a drive through order
                } else if (type == 2) {
                    order = new Onsite(s.nextLine(), Integer.parseInt(s.nextLine()));
                    //Makes order an onsite order
                } else {
                    order = new Phone(s.nextLine(), Integer.parseInt(s.nextLine()), s.nextLine());
                    //Makes order a phone order
                }
                order.setItemID(toIntArray(s.nextLine())); //adds ItemID's to order
                order.setQuantities(toIntArray(s.nextLine())); //adds Quantities to order
                order.setSkipped(Integer.parseInt(s.nextLine())); //sets skipped count to order
                order.setComplete(s.nextLine());//sets the order completion status
                fileOrderArrayList.add(order); //adds order to array list
                orderData.getOrderList().add(order);
            }
            try {
                new FileWriter(file,false).close();
                //Clears file and closes it
            } catch (IOException e) {
                System.out.println("Cant delete");
                throw new RuntimeException(e);
            }
        }

        /*public Order getOrder(int i) {
            return fileOrderArrayList.get(i);
        }*/

        public int getOrdersArrayListLength() {
            return fileOrderArrayList.size();
        }  //Returns size of array list

        public void removeOrder(String s) {
            for (int i = 0; i < fileOrderArrayList.size(); i++) {
                //Loops through the array list
                if (fileOrderArrayList.get(i).getName().equals(s)) {
                    //If the name of the order equal the name to remove, remove it
                    fileOrderArrayList.remove(i);
                    System.out.println("Remove");
                }
            }
        }


        public static void main(String[] args) throws IOException {
            //Creates test case
            FileWriterController f = new FileWriterController();
            Order order = new Order("Order1 DD", 1, 4);
            Order order1 = new Order("Order2 DT", 2, 2);
            Order order2 = new Order("Order3 OS", 3, 1);
            order.addItem(2, 3);
            order.completeOrder();
            order.addItem(6, 7);
            order1.addItem(3, 2);
            order2.addItem(1, 1);

            fileOrderArrayList.add(order);
            fileOrderArrayList.add(order1);
            fileOrderArrayList.add(order2);

         f.writeToFile();

        f.readInFile();


         for(int i = 0; i< fileOrderArrayList.size(); i++){
             System.out.println(fileOrderArrayList.get(i).getName());
         }

          /*  Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                System.out.println(s.nextLine());
            }*/

            System.out.println("done");

        }
    }



