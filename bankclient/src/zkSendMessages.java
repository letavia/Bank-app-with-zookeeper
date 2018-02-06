import org.apache.zookeeper.ZooKeeper;

public abstract interface zkSendMessages
{
  public abstract void sendAdd(ZooKeeper zk, zkClient paramClient);

  //public abstract void sendRead(ZooKeeper zk, Integer paramInteger); handled by local zk server

  public abstract void sendUpdate(ZooKeeper zk, zkClient paramClient);

  public abstract void sendDelete(ZooKeeper zk, Integer paramInteger);

  //public abstract void sendCreateBank(ZooKeeper zk, zkClientDB paramClientDB); handled by local zk server
}
