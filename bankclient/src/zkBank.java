import java.io.PrintStream;
import java.util.HashMap;

import org.apache.zookeeper.ZooKeeper;

public class zkBank {

  public zkClientDB clientDB;
  public zkSendMessagesBank sendMessages;
  HashMap<Integer, zkClient> clientDBHM;

  public zkBank() {
    clientDB = new zkClientDB();
    sendMessages = new zkSendMessagesBank();
  }

  public synchronized void handleReceiverMsg(zkOperationBank operation)
  {
    switch (operation.getOperation()) {
    case CREATE_CLIENT:
      clientDB.createClient(operation.getClient());
      clientDB.clientDBHM.put(Integer.valueOf(operation.getClient().getAccountNumber()), operation.getClient());
      break;
    case READ_CLIENT:
      clientDB.readClient(operation.getAccountNumber());
      break;
    case UPDATE_CLIENT:
      clientDB.updateClient(operation.getClient().getAccountNumber(), operation.getClient().getBalance());
      clientDB.clientDBHM.put(Integer.valueOf(operation.getClient().getAccountNumber()), operation.getClient());
      break;
    case DELETE_CLIENT:
      clientDB.deleteClient(operation.getAccountNumber());
      clientDB.clientDBHM.remove(operation.getAccountNumber());
      break;
    case CLIENT_DB:       
      clientDB.readClientDB(operation.getClientDB());
    }
    System.out.println(operation.getOperation() + " COMPLETE");
  }

  public boolean createClient(ZooKeeper zk, zkClient client)
  { 
	  boolean isCorrect = clientDB.createClient(client); 
	  if (isCorrect == true) {
		  sendMessages.sendAdd(zk, client);
	  }
    return isCorrect;   
  }

  public zkClient readClient(Integer accountNumber)
  {
    return clientDB.readClient(accountNumber);
  }

  public boolean updateClient(ZooKeeper zk, int accNumber, int balance) {
    boolean isCorrect = clientDB.updateClient(accNumber, balance);
    if (isCorrect == true) {
    	sendMessages.sendUpdate(zk, clientDB.readClient(Integer.valueOf(accNumber)));
    }
    return isCorrect;
  }

  public boolean deleteClient(ZooKeeper zk, Integer accountNumber) {
	boolean isCorrect = clientDB.deleteClient(accountNumber); 
	if (isCorrect == true) {
		sendMessages.sendDelete(zk, accountNumber);
	}
    return isCorrect;
  }
  
  public boolean readClientDB() {
    return clientDB.readClientDB(clientDB);
  }

  public String toString() {
    String string = null;
    string = clientDB.toString();
    return string;
  }
}
