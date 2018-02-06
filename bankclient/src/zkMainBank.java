
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

import org.apache.zookeeper.ZooKeeper;

public class zkMainBank {
	
  private zkBank bank;

  public zkClient readClient(Scanner sc)
  {
    int accNumber = 0;
    String name = null;
    int balance = 0;

    System.out.print(">>> Enter account number (int) = ");
    if (sc.hasNextInt()) {
      accNumber = sc.nextInt();
    } else {
      System.out.println("The text provided is not an integer");
      sc.next();
      return null;
    }

    System.out.print(">>> Enter name (String) = ");
    name = sc.next();

    System.out.print(">>> Enter balance (int) = ");
    if (sc.hasNextInt()) {
      balance = sc.nextInt();
    } else {
      System.out.println("The text provided is not an integer");
      sc.next();
      return null;
    }
    return new zkClient(accNumber, name, balance);
  }

  public zkMainBank(ZooKeeper zk, zkBank bank) 
  {
    boolean correct = false;
    int menuKey = 0;
    boolean exit = false;
    Scanner sc = new Scanner(System.in);
    int accNumber = 0;
    int balance = 0;
    zkClient client = null;
    boolean status = false;
    
    while (!exit) {
      try {
        correct = false;
        menuKey = 0;
        while (!correct) {
          System.out.println(">>> Enter Client Option: 1) Create. 2) Read. 3) Update. 4) Delete. 5) Client DB. 6) Exit");
          if (sc.hasNextInt()) {
            menuKey = sc.nextInt();
            correct = true;
          } else {
            sc.next();
            System.out.println("The text provided is not an integer");
          }
        }

        switch (menuKey) {
        case 1:
          bank.createClient(zk, readClient(sc));
          break;
        case 2:
          System.out.print(">>> Enter account number (int) = ");
          if (sc.hasNextInt()) {
            accNumber = sc.nextInt();
            client = bank.readClient(Integer.valueOf(accNumber));
            System.out.println(client);
          } else {
            System.out.println("The text provided is not an integer");
            sc.next();
          }
          break;
        case 3:
          System.out.print(">>> Enter account number (int) = ");
          if (sc.hasNextInt()) {
            accNumber = sc.nextInt();
          } else {
            System.out.println("The text provided is not an integer");
            sc.next();
          }
          System.out.print(">>> Enter balance (int) = ");
          if (sc.hasNextInt()) {
            balance = sc.nextInt();
          } else {
            System.out.println("The text provided is not an integer");
            sc.next();
          }
          bank.updateClient(zk, accNumber, balance);
          break;
        case 4:
          System.out.print(">>> Enter account number (int) = ");
          if (sc.hasNextInt()) {
            accNumber = sc.nextInt();
            status = bank.deleteClient(zk, Integer.valueOf(accNumber));
          } else {
            System.out.println("The text provided is not an integer");
            sc.next();
          }
          break;
        case 5:
          System.out.println(bank.toString());        
          break;
        case 6:
          System.out.println("Exit application");
          System.exit(0);   
        }
      }
      catch (Exception e)
      {
        System.out.println("Exception at Main. Error read data");
      }
    }
    sc.close();
  }
}
