

public abstract interface zkOperation<Node, Data>
{
  public abstract zkOperationEnum getOperation();

  public abstract Node getNode();

  public abstract Node[] getNodeTable();

  public abstract Data getData();

  public abstract Data[] getDataMap();
}
