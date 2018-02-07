import java.io.IOException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class zkSendMessagesBank implements zkSendMessages {
  public zkSendMessagesBank() {
  }

  public byte[] convertToBytes(zkOperationBank operation) throws IOException {
	  byte[] data = SerializationUtils.serialize(operation);
	  return data;
  }
  
  public zkOperationBank convertToObject(byte[] receivedByte) throws IOException, ClassNotFoundException {
	  zkOperationBank obj = (zkOperationBank) SerializationUtils.deserialize(receivedByte);
	  return obj;
  }
  
  public void sendMessage(ZooKeeper zk, byte[] operation) {	  
	  try {
		  zk.create("/operations/op-", operation, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    } catch (Exception e) {
      System.err.println(e);
      System.out.println("Error when sending message");
      e.printStackTrace();
    }
  }

  public void sendAdd(ZooKeeper zk, zkClient client) {
    zkOperationBank operation = new zkOperationBank(zkOperationEnum.CREATE_CLIENT, client);
    try {
		byte[] sendByte = convertToBytes(operation);
		sendMessage(zk, sendByte);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }


  public void sendUpdate(ZooKeeper zk, zkClient client) {
    zkOperationBank operation = new zkOperationBank(zkOperationEnum.UPDATE_CLIENT, client);
    try {
		byte[] sendByte = convertToBytes(operation);
		sendMessage(zk, sendByte);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  public void sendDelete(ZooKeeper zk, Integer accountNumber) {
    zkOperationBank operation = new zkOperationBank(zkOperationEnum.DELETE_CLIENT, accountNumber);
    try {
		byte[] sendByte = convertToBytes(operation);
		sendMessage(zk, sendByte);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

}
