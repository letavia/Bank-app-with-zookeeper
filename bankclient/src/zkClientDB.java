import java.util.HashMap;

public class zkClientDB implements java.io.Serializable {
  private static final long serialVersionUID = 1L;
  public HashMap<Integer, zkClient> clientDBHM;

  public zkClientDB() {
    clientDBHM = new HashMap<Integer, zkClient>();
    initMembers();
  }

  public HashMap<Integer, zkClient> getClientDB() {
    return clientDBHM;
  }
  
  public void initMembers() {
	  clientDBHM.put(1, new zkClient(1, "Bill Gates", 100));
	  clientDBHM.put(2, new zkClient(2, "Jeff Bezos", 200));
	  clientDBHM.put(3, new zkClient(3, "Mark Zuckerberg", 300));
      clientDBHM.put(4, new zkClient(4, "Larry Page", 400));
      clientDBHM.put(5, new zkClient(5, "Sergey Brin", 500));
      clientDBHM.put(6, new zkClient(6, "Larry Ellison", 600)); 
  }

  public boolean createClient(zkClient client) {
    if (clientDBHM.containsKey(Integer.valueOf(client.getAccountNumber()))) {
      System.out.println("Account already existing");
      return false;
    }
    return true;
  }

  public zkClient readClient(Integer accountNumber)
  {
    if (clientDBHM.containsKey(accountNumber)) {
      return (zkClient)clientDBHM.get(accountNumber);
    }
    return null;
  }

  public boolean updateClient(int accNumber, int balance)
  {
    if (clientDBHM.containsKey(Integer.valueOf(accNumber))) {
      zkClient client = (zkClient)clientDBHM.get(Integer.valueOf(accNumber));
      client.setBalance(balance);
      return true;
    }
    System.out.println("Account not existing");
    return false;
  }

  public boolean deleteClient(Integer accountNumber)
  {
    if (clientDBHM.containsKey(accountNumber)) {
      return true;
    }
    System.out.println("Account not existing");
    return false;
  }

  public boolean readClientDB (zkClientDB clientDB)
  {
    clientDBHM = clientDB.getClientDB();
    System.out.println("ClientDB:" + clientDBHM.toString());
    return true;
  }

  public String toString() {
    String aux = new String();
    for (java.util.Map.Entry<Integer, zkClient> entry : clientDBHM.entrySet()) {
      aux = aux + ((zkClient)entry.getValue()).toString() + "\r\n";
    }
    return aux;
  }
}
