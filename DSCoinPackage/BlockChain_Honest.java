package DSCoinPackage;

import java.math.BigInteger;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    if(lastBlock == null){
      CRF crf = new CRF(64);
      String tempnonce = "1000000000";
      BigInteger tempBigInteger = new  BigInteger(tempnonce);
      String topVal = "9999999999";
      BigInteger Topval = new BigInteger(topVal);
      long a = 1;

      String tempdgst = crf.Fn(start_string + "#" + newBlock.trsummary + "#" + tempnonce);

      while(!(tempdgst.charAt(0) == 0 && tempdgst.charAt(1) == 0 && tempdgst.charAt(2) == 0 && tempdgst.charAt(3) == 0) && tempBigInteger.compareTo(Topval) == -1 ){
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

      while(!(tempdgst.charAt(0) == 0 && tempdgst.charAt(1) == 0 && tempdgst.charAt(2) == 0 && tempdgst.charAt(3) == 0) && tempBigInteger.compareTo(Topval) == -1){
        tempBigInteger = tempBigInteger.add(BigInteger.valueOf(a));

        tempnonce = String.valueOf(tempBigInteger);

        tempdgst = crf.Fn(lastBlock.dgst + "#" + newBlock.trsummary + "#" + tempnonce);
      }

      newBlock.dgst = tempdgst;
      newBlock.nonce = tempnonce;
      newBlock.previous = lastBlock;
      newBlock.next = null;
      lastBlock.next = newBlock;
      lastBlock = newBlock;
    }
  }
}
