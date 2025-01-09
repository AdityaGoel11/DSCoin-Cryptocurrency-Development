package DSCoinPackage;

import HelperClasses.MerkleTree;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public TransactionBlock next;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  public TransactionBlock(Transaction[] t) {
    int ctr = 0;
    trarray = new Transaction[t.length];

    for(Transaction i : t){
      trarray[ctr] = new Transaction(i);
      ctr++;
    }

    previous = null;
    next = null;
    dgst = null;
    Tree = new MerkleTree();

    Tree.Build(trarray);

    trsummary = Tree.rootnode.val;
  }

  public boolean checkTransaction(Transaction t) {
    boolean found = false;
    if (t.coinsrc_block == null) {
      return true;
    } else {
      for (Transaction i : t.coinsrc_block.trarray) {
        if (i.coinID == t.coinID) {
          found = true;
          if (i.Destination.UID == t.Source.UID) {
            break;
          } else {
            return false;
          }
        }
      }
    }

    if (found == false) {
      return false;
    }

    TransactionBlock curBlock;
    curBlock = this.previous;

    while (curBlock != t.coinsrc_block) {
      for (Transaction i : curBlock.trarray) {
        if (i.coinID == t.coinID) {
          return false;
        }
      }
      curBlock = curBlock.previous;
    }

    return true;
  }
}
