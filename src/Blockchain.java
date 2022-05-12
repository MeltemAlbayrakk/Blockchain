import java.sql.Array;
import java.util.Date;
import java.security.MessageDigest;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Blockchain {

    public static class Block{

        private int Id;
        public String hash;
        public String previousHash;
        private String data;
        private long timeStamp;
        private int nonce;
        private String transaction;



        public Block(int Id,String transaction,String data,String previousHash){
            this.data=data;
            this.previousHash = previousHash;
            this.timeStamp= new Date().getTime();
            this.hash = calculateHash();
            this.Id= Id;
            this.transaction=transaction;

        }

        public String calculateHash(){
            String calculatedhash = StringUtil.applySha256(Integer.toString(Id)+transaction+previousHash +
                    Long.toString(timeStamp)+
                    data+Integer.toString(nonce));

            return calculatedhash;


        }
        public void mineBlock(int difficulty){
            String target = new String(new char[difficulty]).replace('\0','0');
            while(!hash.substring(0,difficulty).equals(target)){
                nonce++;
                hash=calculateHash();
            }

            System.out.println("Block Mined !: "+hash);
        }
    }

    public class StringUtil {
        public static String applySha256(String input){
            try{
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                    byte[] hash =digest.digest(input.getBytes("UTF-8"));
                    StringBuffer hexString = new StringBuffer();
                    for (int i =0;i<hash.length;i++) {
                        String hex =Integer.toHexString(0xff & hash[i]);
                        if(hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }

              return hexString.toString();
            }

          catch(Exception e) {
              throw new RuntimeException(e);
          }
        }
    }


    public static Boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');

        for (int i=1;i<blockchain.size();i++){
            currentBlock = blockchain.get(i);
            previousBlock= blockchain.get(i-1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("suanki hashler eşit değil");
                return false;
            }
            if (!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("önceki hashler esit değil");
                return false;
            }
            if (!currentBlock.hash.substring(0,difficulty).equals(hashTarget)){
                System.out.println("mine edilemedi.");
                return false;
            }
        }
        return true;
    }


    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static void main(String[] args) {
    blockchain.add(new Block(0,"transaction1" ,"ben blok 1","0"));
    System.out.println("Blok 1 oluşturuluyor...");
    blockchain.get(0).mineBlock(difficulty);

    blockchain.add(new Block(1,"transaction2","ben block 2 : ",blockchain.get(blockchain.size()-1).hash));
    System.out.println("Blok 2 oluşturuluyor...");
   blockchain.get(1).mineBlock(difficulty);

    blockchain.add(new Block(2,"transaction3", "ben block 3 :",blockchain.get(blockchain.size()-1).hash));
    System.out.println("Blok 3 oluşturuluyor...");
    blockchain.get(2).mineBlock(difficulty);

    System.out.println("\n Blockchain is valid: " + isChainValid());


    String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    System.out.println("\n The Block Chain: ");
    System.out.println(blockchainJson);


    }
}
