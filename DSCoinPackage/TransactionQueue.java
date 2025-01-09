package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if(numTransactions == 0){
      transaction.Previous = null;
      transaction.Next = null;
      firstTransaction = transaction;
      lastTransaction = transaction;
      numTransactions++;
    }
    else{
      transaction.Next = null;
      transaction.Previous = lastTransaction;
      lastTransaction.Next = transaction;
      lastTransaction = transaction;
      numTransactions = numTransactions + 1;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    if(numTransactions == 0){
      throw new EmptyQueueException();
    }

    else{
      numTransactions--;
      Transaction tempTr = new Transaction();
      tempTr = firstTransaction;
      firstTransaction = firstTransaction.Next;
      firstTransaction.Previous = null;

      return tempTr;
    }
  }

  public int size() {
    return numTransactions;
  }
}
