package DSCoinPackage;

import HelperClasses.*;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int i = 0;
    String coinID = "100000";
    Members Moderator = new Members();
    Moderator.UID = "Moderator";
    int latestCoinID = Integer.valueOf(coinID);
    int k = coinCount/DSObj.bChain.tr_count;

    while(i < coinCount){
      int j = i % DSObj.memberlist.length;
      Transaction nextTransaction = new Transaction();
      Pair<String, TransactionBlock> destNextCoin = new Pair<>();

      nextTransaction.Source = Moderator;
      nextTransaction.coinsrc_block = null;
      nextTransaction.coinID = String.valueOf(latestCoinID);
      nextTransaction.Destination = DSObj.memberlist[j];

      destNextCoin.first = String.valueOf(latestCoinID);
      destNextCoin.second = null;

      DSObj.pendingTransactions.AddTransactions(nextTransaction);
      DSObj.memberlist[j].mycoins.add(destNextCoin);

      i++;
      latestCoinID++;
    }

    for(int ctr = 0; ctr < k; ctr++){
      Transaction[] BlockArray = new Transaction[DSObj.bChain.tr_count];

      for(int x = 0; x < DSObj.bChain.tr_count; x++){
        try{
        BlockArray[x] = DSObj.pendingTransactions.RemoveTransaction();
        }
        catch(EmptyQueueException e){} 
      }

      TransactionBlock nextTransactionBlock = new TransactionBlock(BlockArray);
      DSObj.bChain.InsertBlock_Honest(nextTransactionBlock);
    }
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    int i = 0;
    String coinID = "100000";
    Members Moderator = new Members();
    Moderator.UID = "Moderator";
    int latestCoinID = Integer.valueOf(coinID);
    int k = coinCount/DSObj.bChain.tr_count;

    while(i < coinCount){
      int j = i % DSObj.memberlist.length;
      Transaction nextTransaction = new Transaction();
      Pair<String, TransactionBlock> destNextCoin = new Pair<>();

      nextTransaction.Source = Moderator;
      nextTransaction.coinsrc_block = null;
      nextTransaction.coinID = String.valueOf(latestCoinID);
      nextTransaction.Destination = DSObj.memberlist[j];

      destNextCoin.first = String.valueOf(latestCoinID);
      destNextCoin.second = null;

      DSObj.pendingTransactions.AddTransactions(nextTransaction);
      DSObj.memberlist[j].mycoins.add(destNextCoin);

      i++;
      latestCoinID++;
    }

    for(int ctr = 0; ctr < k; ctr++){
      Transaction[] BlockArray = new Transaction[DSObj.bChain.tr_count];

      for(int x = 0; x < DSObj.bChain.tr_count; x++){
        try{
        BlockArray[x] = DSObj.pendingTransactions.RemoveTransaction();
        }
        catch(EmptyQueueException e){} 
      }

      TransactionBlock nextTransactionBlock = new TransactionBlock(BlockArray);
      DSObj.bChain.InsertBlock_Malicious(nextTransactionBlock);
    }
  }
}
