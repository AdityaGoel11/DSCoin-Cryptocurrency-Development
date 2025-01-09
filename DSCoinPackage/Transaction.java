package DSCoinPackage;

public class Transaction {

  public String coinID;
  public Members Source;
  public Members Destination;
  public TransactionBlock coinsrc_block;
  public Transaction Next;
  public Transaction Previous;

  public Transaction(Transaction t){
    coinID = t.coinID;
    Source = t.Source;
    Destination = t.Destination;
    coinsrc_block = t.coinsrc_block;
  }

  public Transaction(){}
}
