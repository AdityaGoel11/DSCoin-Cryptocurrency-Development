package DSCoinPackage;

import java.util.*;
import HelperClasses.*;

public class Members {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    Transaction newTransaction = new Transaction();
    int i = 0;

    newTransaction.coinID = mycoins.get(0).first;
    newTransaction.Destination.UID = destUID;
    newTransaction.Source.UID = UID;
    newTransaction.coinsrc_block = mycoins.get(0).second;

    mycoins.remove(0);

    if (in_process_trans[i] == null) {
      in_process_trans[i] = new Transaction(newTransaction);
    } else {
      for (Transaction z : in_process_trans) {
        if (in_process_trans[i + 1] == null) {
          in_process_trans[i + 1] = new Transaction(newTransaction);
          break;
        }
        i++;
      }
    }

    DSobj.pendingTransactions.AddTransactions(newTransaction);

  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Transaction newTransaction = new Transaction();
    int i = 0;

    newTransaction.coinID = mycoins.get(0).first;
    newTransaction.Destination.UID = destUID;
    newTransaction.Source.UID = UID;
    newTransaction.coinsrc_block = mycoins.get(0).second;

    mycoins.remove(0);

    while (true) {
      if (in_process_trans[i] == null) {
        in_process_trans[i] = newTransaction;
        break;
      } else {
        i++;
      }
    }

    DSobj.pendingTransactions.AddTransactions(newTransaction);

  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend(Transaction tobj,
      DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock curBlock = DSObj.bChain.lastBlock;
    Transaction curTransaction = DSObj.bChain.lastBlock.trarray[0];
    TransactionBlock tB = curBlock;
    int t = 0;

    while (curBlock != null) {
      t = 0;
      for (Transaction i : curBlock.trarray) {
        if (i == tobj) {
          tB = curBlock;
          break;
        } else {
          t++;
        }
      }
      curBlock = curBlock.previous;
    }

    if (curBlock == null) {
      throw new MissingTransactionException();
    }

    ArrayList<Pair<String, String>> pathlist = new ArrayList<>();

    ArrayList<Pair<String, String>> blockPathList = new ArrayList<>();

    Pair<List<Pair<String, String>>, List<Pair<String, String>>> output = new Pair<>();

    TreeNode currNode = new TreeNode();
    currNode = curBlock.Tree.rootnode;
    int num = curBlock.Tree.numdocs;
    int s = t;
    int l = DSObj.bChain.tr_count;
    int v = (int) (Math.log(l) / Math.log(2));
    int a = 0;

    while (a < v) {
      if (s < num / 2) {
        currNode = currNode.left;

      } else {
        currNode = currNode.right;
      }
      num = num / 2;
      s = s % num;
      a++;
    }

    TreeNode currnode = new TreeNode();
    currnode = currNode;

    while (currnode.parent != null) {
      Pair<String, String> tobeaddedPair = new Pair<>();

      if (currnode == currnode.parent.left) {
        tobeaddedPair.first = currnode.val;
        tobeaddedPair.second = currnode.parent.right.val;
        pathlist.add(tobeaddedPair);
      }

      else {
        tobeaddedPair.first = currnode.parent.left.val;
        tobeaddedPair.second = currnode.val;
        pathlist.add(tobeaddedPair);
      }
      currnode = currnode.parent;
    }

    Pair<String, String> tobeaddedPair = new Pair<>();
    tobeaddedPair.first = curBlock.Tree.rootnode.val;
    tobeaddedPair.second = null;
    pathlist.add(tobeaddedPair);

    Pair<String, String> firstblockpair = new Pair<>();
    firstblockpair.first = tB.previous.dgst;
    firstblockpair.second = null;
    blockPathList.add(firstblockpair);

    while (curBlock.next != null) {
      Pair<String, String> nextblockpair = new Pair<>();
      nextblockpair.first = curBlock.dgst;
      nextblockpair.second = curBlock.previous.dgst + "#" + curBlock.trsummary + "#" + curBlock.nonce;
      blockPathList.add(nextblockpair);

      curBlock = curBlock.next;
    }

    int x = 0;

    while (in_process_trans[x] != tobj) {
      x++;
    }

    in_process_trans[x] = null;
    int z = 0;

    Pair<String, TransactionBlock> nextmycoin = new Pair<>();
    nextmycoin.first = tobj.coinID;
    nextmycoin.second = tB;

    for (Pair<String, TransactionBlock> y : tobj.Destination.mycoins) {
      if (Integer.valueOf(tobj.coinID) < Integer.valueOf(y.first)) {
        tobj.Destination.mycoins.add(z, nextmycoin);
      } else {
        z++;
      }
    }

    output.first = pathlist;
    output.second = blockPathList;

    return output;
  }

  public void MineCoin(DSCoin_Honest DSObj) {
    int i = 0;
    Transaction[] transactions = new Transaction[100];
    boolean duplicate = false;

    while (i < DSObj.bChain.tr_count - 1) {
      try {
        if (i > 0) {
          for (int j = 0; j < i; j++) {
            if (transactions[j].coinID == DSObj.pendingTransactions.firstTransaction.coinID) {
              DSObj.pendingTransactions.RemoveTransaction();
              duplicate = true;
              break;
            }
          }
        }

        if (duplicate == false) {
          transactions[i] = DSObj.pendingTransactions.RemoveTransaction();
          i++;
        }

      } catch (EmptyQueueException e) {
        System.out.println("Empty Queue");
      }
    }

    int newCoinIDint = Integer.valueOf(DSObj.latestCoinID);
    newCoinIDint++;
    String newCoinID = String.valueOf(newCoinIDint);
    DSObj.latestCoinID = String.valueOf(newCoinIDint);

    Transaction minerRewardTransaction = new Transaction();
    minerRewardTransaction.Source = null;
    minerRewardTransaction.coinsrc_block = null;
    minerRewardTransaction.Destination = this;
    minerRewardTransaction.Next = null;
    minerRewardTransaction.Previous = null;
    minerRewardTransaction.coinID = newCoinID;

    transactions[i] = minerRewardTransaction;

    TransactionBlock tB = new TransactionBlock(transactions);
    DSObj.bChain.InsertBlock_Honest(tB);

    Pair<String, TransactionBlock> mynewcoin = new Pair<>();
    mynewcoin.first = newCoinID;
    mynewcoin.second = tB;
    mycoins.add(mynewcoin);
  }

  public void MineCoin(DSCoin_Malicious DSObj) {
    int i = 0;
    Transaction[] transactions = new Transaction[100];
    boolean duplicate = false;

    while (i < DSObj.bChain.tr_count - 1) {
      try {
        if (i > 0) {
          for (int j = 0; j < i; j++) {
            if (transactions[j].coinID == DSObj.pendingTransactions.firstTransaction.coinID) {
              DSObj.pendingTransactions.RemoveTransaction();
              duplicate = true;
              break;
            }
          }
        }

        if (duplicate == false) {
          transactions[i] = DSObj.pendingTransactions.RemoveTransaction();
          i++;
        }

      } catch (EmptyQueueException e) {
        System.out.println("Empty Queue");
      }
    }

    int newCoinIDint = Integer.valueOf(DSObj.latestCoinID);
    newCoinIDint++;
    String newCoinID = String.valueOf(newCoinIDint);
    DSObj.latestCoinID = String.valueOf(newCoinIDint);

    Transaction minerRewardTransaction = new Transaction();
    minerRewardTransaction.Source = null;
    minerRewardTransaction.coinsrc_block = null;
    minerRewardTransaction.Destination = this;
    minerRewardTransaction.Next = null;
    minerRewardTransaction.Previous = null;
    minerRewardTransaction.coinID = newCoinID;

    transactions[i] = minerRewardTransaction;

    TransactionBlock tB = new TransactionBlock(transactions);
    DSObj.bChain.InsertBlock_Malicious(tB);

    Pair<String, TransactionBlock> mynewcoin = new Pair<>();
    mynewcoin.first = newCoinID;
    mynewcoin.second = tB;
    mycoins.add(mynewcoin);
  }
}
