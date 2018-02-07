

import java.io.Serializable;

public class zkOperationBank implements Serializable
{
  private static final long serialVersionUID = 1L;
  private zkOperationEnum operation;
  private zkClient client = null;
  private Integer accountNumber = Integer.valueOf(0);
  private zkClientDB clientDB = null;

  
  public zkOperationBank(zkOperationEnum operation, zkClient client)
  {
    this.operation = operation;
    this.client = client;
  }


  public zkOperationBank(zkOperationEnum operation, Integer accountNumber)
  {
    this.operation = operation;
    this.accountNumber = accountNumber;
  }

  public zkOperationBank(zkOperationEnum operation, zkClientDB clientDB)
  {
    this.operation = operation;
    this.clientDB = clientDB;
  }

  public zkOperationEnum getOperation()
  {
    return operation;
  }

  public void setOperation(zkOperationEnum operation) {
    this.operation = operation;
  }

  public zkClient getClient() {
    return client;
  }

  public void setClient(zkClient client) {
    this.client = client;
  }

  public Integer getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Integer accountNumber) {
    this.accountNumber = accountNumber;
  }

  public zkClientDB getClientDB() {
    return clientDB;
  }

  public void setClientDB(zkClientDB clientDB) {
    this.clientDB = clientDB;
  }

  public String toString()
  {
    String string = null;

    string = "zkOperationBank [operation=" + operation;
    if (client != null) {
    	string = string + ", client=" + client.toString();
    } else {
    string = string + ", accountNumber=" + accountNumber + "]\n";
    if (clientDB != null) { 
    	string = string + clientDB.toString();
    }
    }
    return string;
  }
}
