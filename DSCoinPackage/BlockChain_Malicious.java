package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;
import java.math.BigInteger;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    if(tB.previous == null){
      CRF crf = new CRF(64);
      String dgstcheck;

      dgstcheck = crf.Fn(start_string + "#" + tB.trsummary + "#" + tB.nonce);

      if(tB.dgst != dgstcheck){
        return false;
      }
      if(tB.dgst.charAt(0) != 0 || tB.dgst.charAt(1) != 0 || tB.dgst.charAt(2) != 0 || tB.dgst.charAt(3) != 0){
        return false;
      }
    }

    else{
      CRF crf = new CRF(64);
      String dgstcheck;

      dgstcheck = crf.Fn(tB.previous.dgst + "#" + tB.trsummary + "#" + tB.nonce);

      if(tB.dgst != dgstcheck){
        return false;
      }
      if(tB.dgst.charAt(0) != 0 || tB.dgst.charAt(1) != 0 || tB.dgst.charAt(2) != 0 || tB.dgst.charAt(3) != 0){
        return false;
      }
    }

    for(Transaction i: tB.trarray){
      if(tB.checkTransaction(i) == false){
        return false;
      }
    }

    MerkleTree tree = new MerkleTree();

    if(tB.trsummary != tree.Build(tB.trarray)){
      return false;
    }

    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    int[] length = new int[100];
    int ctr = 0;
    int longestlength = 0;
    TransactionBlock reqBlock = lastBlocksList[0];

    for(TransactionBlock i: lastBlocksList){
      TransactionBlock curBlock = i;
      length[ctr] = 0;

      while(checkTransactionBlock(curBlock)){
        length[ctr]++;
        curBlock = curBlock.previous;
      }

      if(longestlength < length[ctr]){
        longestlength = length[ctr];
        reqBlock = i;
      }

      ctr++;
    }

    return reqBlock;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock = FindLongestValidChain();

    if(lastBlock == null){
      CRF crf = new CRF(64);
      String tempnonce = "1000000000";
      BigInteger tempBigInteger = new  BigInteger(tempnonce);
      String topVal = "9999999999";
      BigInteger Topval = new BigInteger(topVal);
      long a = 1;

      String tempdgst = crf.Fn(start_string + "#" + newBlock.trsummary + "#" + tempnonce);

      while(!(tempdgst.charAt(0) == 0 && tempdgst.charAt(1) == 0 && tempdgst.charAt(2) == 0 && tempdgst.charAt(3) == 0)  && tempBigInteger.compareTo(Topval) == -1){
        tempBigInteger = tempBigInteger.add(BigInteger.valueOf(a));

        tempnonce = String.valueOf(tempBigInteger);

        tempdgst = crf.Fn(start_string + "#" + newBlock.trsummary + "#" + tempnonce);
      }

      newBlock.dgst = tempdgst;
      newBlock.nonce = tempnonce;
      newBlock.previous = null;
      newBlock.next = null;
      lastBlock = newBlock;
      
    }
    else{
      CRF crf = new CRF(64);
      String tempnonce = "1000000000";
      BigInteger tempBigInteger = new  BigInteger(tempnonce);
      String topVal = "9999999999";
      BigInteger Topval = new BigInteger(topVal);
      long a = 1;

      String tempdgst = crf.Fn(lastBlock.dgst + "#" + newBlock.trsummary + "#" + tempnonce);

      while(!(tempdgst.charAt(0) == 0 && tempdgst.charAt(1) == 0 && tempdgst.charAt(2) == 0 && tempdgst.charAt(3) == 0)  && tempBigInteger.compareTo(Topval) == -1){
        tempBigInteger = tempBigInteger.add(BigInteger.valueOf(a));

        tempnonce = String.valueOf(tempBigInteger);

        tempdgst = crf.Fn(lastBlock.dgst + "#" + newBlock.trsummary + "#" + tempnonce);
      }

      newBlock.dgst = tempdgst;
      newBlock.nonce = tempnonce;
      newBlock.previous = lastBlock;
      newBlock.next = null;
      lastBlock.next = newBlock;

      int y = 0;
      for(TransactionBlock z : lastBlocksList){
        if(z.dgst == lastBlock.dgst){
          lastBlocksList[y] = newBlock;
        }
        y++;
      }

      lastBlock = newBlock;
    }
  }
}
