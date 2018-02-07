import org.apache.zookeeper.ZooKeeper;

public abstract interface zkSendMessages
{
  public abstract void sendAdd(ZooKeeper zk, zkClient paramClient);

  public abstract void sendUpdate(ZooKeeper zk, zkClient paramClient);

  public abstract void sendDelete(ZooKeeper zk, Integer paramInteger);

}
