

import java.io.Serializable;

public class zkClient implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int accountNumber;
  private String name;
  private int balance;

  public zkClient(int accountNumber, String name, int balance)
  {
    this.accountNumber = accountNumber;
    this.name = name;
    this.balance = balance;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public String toString()
  {
    return "[" + accountNumber + ", " + name + ", " + balance + "]";
  }

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + accountNumber;
    result = 31 * result + balance;
    result = 31 * result + (name == null ? 0 : name.hashCode());
    return result;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    zkClient other = (zkClient)obj;
    if (accountNumber != accountNumber)
      return false;
    if (balance != balance)
      return false;
    if (name == null) {
      if (name != null)
        return false;
    } else if (!name.equals(name))
      return false;
    return true;
  }
}
