import java.util.*;
import java.io.*;

public class Md51
{
    
    public static void pause()
    {
          System.out.println("Press Any Key To Continue...");
          new java.util.Scanner(System.in).nextLine();
    }
        
    public static byte[] and(byte[] s1, byte[] s2)
    {
        byte[] result = new byte[s1.length];
        for(int i = 0; i < s1.length; i++)
        {
            if(s1[i]+s2[i] == 2) result[i] = 1;
            else result[i] = 0;
        }
        return result;
    }
    
    public static byte[] not(byte[] s1)
    {
        byte[] result = new byte[s1.length];
        for(int i = 0; i < s1.length; i++)
        {
            if(s1[i] == 1) result[i] = 0;
            else result[i] = 1;
        }
        return result;
    }
    
    public static byte[] or(byte[] s1, byte[] s2)
    {
        byte[] result = new byte[s1.length];
        for(int i = 0; i < s1.length; i++)
        {
            if(s1[i]+s2[i] == 0) result[i] = 0;
            else result[i] = 1;
        }
        return result;
    }
    
    public static byte[] xor(byte[] s1, byte[] s2)
    {
        byte[] result = new byte[s1.length];
        for(int i = 0; i < s1.length; i++)
        {
            if(s1[i] == s2[i]) result[i] = 0;
            else result[i] = 1;
        }
        return result;
    }
    
    public static byte[] concat(byte[] s1, byte[] s2)
    {
        byte[] result = new byte[32];
        byte rest = 0;
        for(int i = 31; i >= 0; i--)
        {
            if((s1[i] + s2[i] + rest) % 2 == 0) result[i] = 0;
            else result[i] = 1;
            if((s1[i] + s2[i] + rest) > 1) rest = 1;
            else rest = 0;
        }
        return result;
    }
    
    public static byte[] round(byte[] s1, int t)
    {
        byte[] result = new byte[s1.length];
        for(int i = 0; i < s1.length - t; i++)
            result[i] = s1[i+t];
        for(int i = s1.length - t; i < s1.length; i++)
            result[i] = s1[i - s1.length + t];
        return result;
    }
    
    public static byte[] Ff(byte[] b, byte[] c, byte[] d)
    {
        //(B and C) or ((not B) and D)
        byte[] result = or(and(b, c), and(not(b), d));
        return result;
    }
    
    public static byte[] Fg(byte[] b, byte[] c, byte[] d)
    {
        //(D and B) or ((not D) and C)
        byte[] result = or(and(d, b), and(not(d), c));
        return result;
    }
    
    public static byte[] Fh(byte[] b, byte[] c, byte[] d)
    {
        //B xor C xor D
        byte[] result = xor(b, xor(c, d));
        return result;
    }
    
    public static byte[] Fi(byte[] b, byte[] c, byte[] d)
    {
        //C xor (B or (not D))
        byte[] result = xor(c, or(b, not(d)));
        return result;
    }
    
    public static byte[] append(byte[] b, byte val)
    {
        byte[] result = new byte[b.length + 1];
        System.arraycopy(b, 0, result, 0, b.length);
        result[result.length-1] = val;
        return result;
    }
    
    public static byte[] append(byte[] b1, byte[] b2)
    {
        byte[] result = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, result, 0, b1.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }
    
    public static byte[] copy(byte[] b)
    {
        byte[] result = new byte[b.length];
        System.arraycopy(b, 0, result, 0, b.length);
        return result;
    }
    
    public static String makeString(byte[] hash)
    {
        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else
                hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }
    
    public static byte[] fromIntToByte(int nb)
    {
        byte[] result = new byte[32];
        if(nb < 0) {result[0] = (byte) 0x01; nb += (256*256*256*128);}//= 2^31
        else result[0] = (byte) 0x00;
        for(int i = 31; i > 0; i--)
        {
            if(nb % 2 == 0) result[i] = (byte) 0x00;
            else result[i] = (byte) 0x01;
            nb /= 2;
        }
        return result;
    }
        
    public static String fromByteToHex(byte[] s1)
    {
        return makeString(shorter(s1));
    }
    
    public static byte[] fromByteToWord(byte[] nb, int start)
    {
        byte[] result = new byte[32];
        for(int i=0; i<32; i++) result[i] = nb[start+i];
        return result;
    }
    
    public static byte[] wider(byte[] nb)
    {
        byte[] result = new byte[nb.length*8];
        
        for(int i = 0; i < nb.length; i++)
        {
            byte tmp = nb[i];
            if(tmp < 0)
            {
                result[i*8] = 1;
                tmp -= 128;
            }
            else result[i*8] = 0;
            for(int j = 64, k = 1; k < 8; j /= 2, k++)
            {
                if(tmp >= j)
                {
                    result[i*8+k] = 1;
                    tmp -= j;
                }
                else result[i*8+k] = 0;
            }
            
        }
        return result;
    }
    
    public static byte[] shorter(byte[] nb)
    {
        byte[] result = new byte[nb.length/8];

        for(int i = 0; i < nb.length/8; i++)
        {
            byte tmp = 0;
            if(nb[i*8] == 1) tmp += 0x80;
            if(nb[i*8+1] == 1) tmp += 0x40;
            if(nb[i*8+2] == 1) tmp += 0x20;
            if(nb[i*8+3] == 1) tmp += 0x10;
            if(nb[i*8+4] == 1) tmp += 0x08;
            if(nb[i*8+5] == 1) tmp += 0x04;
            if(nb[i*8+6] == 1) tmp += 0x02;
            if(nb[i*8+7] == 1) tmp += 0x01;
            result[i] = tmp;
        }
        
        return result;
    }
    
    public static String generate(int length)
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String pass = "";
        for(int x=0;x<length;x++)
        {
           int i = (int)Math.floor(Math.random() * 62);
           pass += chars.charAt(i);
        }
        return pass;
    }
    
    public static byte[] Pad(byte[] key, int blockSize)
    {
        byte[] result = new byte[blockSize*8];
        System.arraycopy(key, 0, result, 0, key.length);
        int i = key.length;
        while(result.length < blockSize*8)
        {
            result[i] = (byte) 0x00;
            i++;
        }
        return result;
    }
    
    public static byte[] key_Pad(int size, boolean flag)
    {
        byte r[] = new byte[size];
        if(flag) for(int i=0; i<size; i++) r[i] ^= 0x36;
        else for(int i=0; i<size; i++) r[i] ^= 0x5c;
        return r;
    }
    
    public static byte[] switchEndian(byte[] s)
    {
        byte[] result = new byte[s.length];
        if(s.length%16!=0) System.out.println("length not divisible by 16!");
        for(int h = 0; h < s.length; h += 32)//6180 0000 <--> 0000 8061
        {
            for(int j = 0; j < 8; j++)
            {
                result[h + 24 + j] = s[h + j];
                result[h + j] = s[h + 24 + j];
            }
            for(int j = 8; j < 16; j++)
            {
                result[h + 8 + j] = s[h + j];
                result[h + j] = s[h + 8 + j];
            }
        }
        return result;
    }
    
    public static byte[] md5(byte[] message3)//pseudobyte
    {
        //s specifies the per-round shift amounts
        int[] s = { 7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21};
        //K = sin(...)
        int[] K = {0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501, 0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, 0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391 };

        //Initialize variables:
        byte[] a1 = fromIntToByte(1732584193);//(0x67452301);   //A
        byte[] b1 = fromIntToByte(-271733879);//((int)0xefcdab89);   //B
        byte[] c1 = fromIntToByte(-1732584194);//((int)0x98badcfe);   //C
        byte[] d1 = fromIntToByte(271733878);//(0x10325476);   //D
        
        //Note: All variables are unsigned 32 bit and wrap modulo 2^32 when calculating
        int originalLength = message3.length/8;

        //Pre-processing: adding a single 1 bit : append "1" bit to message    
        // Notice: the input bytes are considered as bits strings,
        //  where the first bit is the most significant bit of the byte.[48]
        message3 = append(message3, (byte)0x01);    

        //Pre-processing: padding with zeros
        //append "0" bit until message length in bits ≡ 448 (mod 512)
        while((message3.length)%512!=448)//message3.length%512?
        {
            message3 = append(message3, (byte)0x00);
        }
        
        //append original length in bits mod 264 to message
        byte[] originalLength3 = fromIntToByte((originalLength*8)%(256*256*256*128));// = 2^31
        
        if(originalLength3.length != 32) System.err.println("error length: "+originalLength3.length);
        
        message3 = append(message3, switchEndian(originalLength3));
        
        byte[] originalLength4 = fromIntToByte((originalLength-(originalLength%(256*256*256*128)))/(256*256*256*128));
        
        message3 = append(message3, switchEndian(originalLength4));
        message3 = switchEndian(message3);
        
        //Process the message in successive 512-bit chunks:
        //for each 512-bit chunk of padded message break chunk into sixteen 32-bit words M[j], 0 ≤ j ≤ 15
        for(int bitBlock = 0; bitBlock < message3.length; bitBlock += 512)
        {           
            //Initialize hash value for this chunk:
            byte[] A = copy(a1);
            byte[] B = copy(b1);
            byte[] C = copy(c1);
            byte[] D = copy(d1);
            
            for(int i = 0;  i < 16; i++)
            {
                byte[] F = Ff(B, C, D);
                int g = bitBlock + (i * 32);
                byte[] K2 = fromIntToByte(K[i]);
                byte[] M = fromByteToWord(message3, g);
                F = concat(concat(F, A), concat(K2, M));
                A = D;
                D = C;
                C = B;
                B = concat(B, round(F, s[i]));
            }
            for(int i = 16; i < 32; i++)
            {
                byte[] F = Fg(B, C, D);
                int g = bitBlock + (((5 * i + 1) % 16) * 32);
                byte[] K2 = fromIntToByte(K[i]);
                byte[] M = fromByteToWord(message3, g);
                F = concat(concat(F, A), concat(K2, M));
                A = D;
                D = C;
                C = B;
                B = concat(B, round(F, s[i]));
            }
            for(int i = 32; i < 48; i++)
            {
                byte[] F = Fh(B, C, D);
                int g = bitBlock + (((3 * i + 5) % 16) * 32);
                byte[] K2 = fromIntToByte(K[i]);
                byte[] M = fromByteToWord(message3, g);
                F = concat(concat(F, A), concat(K2, M));
                A = D;
                D = C;
                C = B;
                B = concat(B, round(F, s[i]));
            }
            for(int i = 48; i < 64; i++)
            {
                byte[] F = Fi(B, C, D);
                int g = bitBlock + (((7 * i) % 16) * 32);
                byte[] K2 = fromIntToByte(K[i]);
                byte[] M = fromByteToWord(message3, g);
                F = concat(concat(F, A), concat(K2, M));
                A = D;
                D = C;
                C = B;
                B = concat(B, round(F, s[i]));
            }  
            //Add this chunk's hash to result so far:
            a1 = concat(a1, A);
            b1 = concat(b1, B);
            c1 = concat(c1, C);
            d1 = concat(d1, D);
        }
        byte digest[] = append(append(a1, b1), append(c1, d1));
        return switchEndian(digest);
    }
    
    public static String hmac(String message, String key)
    {
        int blockSize = 64;
        
        byte[] Key;
        Key = wider(key.getBytes());
        
        
        //Keys longer than blockSize are shortened by hashing them
        if (Key.length > blockSize*8) Key = md5(Key);
        //Keys shorter than blockSize are padded to blockSize by padding with zeros on the right
        else if (Key.length < blockSize*8) Key = Pad(Key, blockSize);
        
        byte[] OKey = wider(key_Pad(blockSize, false));
        byte[] IKey = wider(key_Pad(blockSize, true)); 
        byte[] o_key_pad = xor(Key, OKey);//xor [0x5c * blockSize]   //Outer padded key
        byte[] i_key_pad = xor(Key, IKey);//xor [0x36 * blockSize]   //Inner padded key
        
        String Hmac = fromByteToHex(md5(append(o_key_pad, md5(append(i_key_pad, wider(message.getBytes()))))));
        return Hmac;
    }
    
    public static void main(String[] args)
    {
        String passwords[] = new String[100];
        String identifiants[] = new String[100];
        String salage[] = new String [100];
     
        System.out.println("\n\n ---- Projet HMAC MD5 Claire Gouessant et Bertrand Duhamel ----");
        System.out.println(    " ---- Projet HMAC MD5     M1 IFM       et     M1 IRV     ----\n");
        System.out.println(" Faites un choix\n 1. Hasher le message en utilisant le MD5\n"  
                + " 2. Afficher 100 messages aléatoires hasher en MD5 avec un identifiant aléatoire\n"
                + " 3. Afficher 100 messages aléatoires hasher en MD5 avec un salage aléatoire avec un identifiant aléatoire\n"
                + " 4. Hasher le message en utilisant le HMAC MD5\n"
                + " 5. Saisir un mot de passe et une clé, résultat: MD5, MD5+sel, HMAC+sel\n"
                + " 6. quitter");
        
        Scanner scan = new Scanner(System.in);
        String toEncode;
        String myKey;
        String choix = "a";
        
        while(!"1".equals(choix) && !"2".equals(choix) && !"3".equals(choix) && !"4".equals(choix) && !"5".equals(choix) && !"6".equals(choix))
        {
            choix = scan.nextLine();
        }
        switch (choix)
        {
            case "1" : 
                System.out.println(" Veuillez saisir le message à hasher :");
                toEncode = scan.nextLine();
                
                System.out.println(" Message : " + toEncode);
                System.out.println("\n MD5 : \n");
                System.out.println(fromByteToHex(md5(wider(toEncode.getBytes()))));
                
                pause();
                main(args);
                
                break;
            case "4" : 
                System.out.println(" Veuillez saisir le message à hasher :");
                toEncode = scan.nextLine();
                System.out.println(" Veuillez saisir la clé :");
                myKey = scan.nextLine();
                
                System.out.println("Hmac: " + hmac(toEncode, myKey));
                pause();
                main(args);
                
                break;
            case "2" : 
                for(int i = 0; i < 100; i++)
                {
                    identifiants[i] = i+1 + generate((int)Math.floor(Math.random() * 10 + 1));
                    passwords[i] = generate((int)Math.floor(Math.random() * 100 + 1)); 
                }
                for (int i = 0; i < 100; i++)
                {
                    System.out.println("Id : " + identifiants[i]);
                    System.out.println("Mdp : " + passwords[i]);
                    System.out.println("Mdp Hashé: " + fromByteToHex(md5(wider(passwords[i].getBytes()))) + "\n"); 
                }
                try (PrintWriter pWriter = new PrintWriter(new FileWriter("sans sel.txt", true))) 
                {
                    
                    for (int i = 0; i < 100; i++)
                    {
                    pWriter.println("Id : " + identifiants[i]);
                    pWriter.println("Mdp : " + passwords[i]);
                    pWriter.println("Mdp Hashé: " + fromByteToHex(md5(wider((passwords[i]+salage[i]).getBytes())))); 
                    }
                    
                }
                catch (IOException e)
                {
                    System.out.println ("Erreur d'écriture dans le fichier");
                }
                
                pause();
                main(args);
                
                break;
            case "3" : 
                for(int i = 0; i < 100; i++)
                {
                    identifiants[i] = i+1 + generate((int)Math.floor(Math.random() * 10 + 1));
                    salage[i] = generate((int)Math.floor(Math.random() * 10 + 1));
                    passwords[i] = generate((int)Math.floor(Math.random() * 100 + 1)); 
                }
                for (int i = 0; i < 100; i++)
                {
                    System.out.println("Id : " + identifiants[i]);
                    System.out.println("Mdp : " + passwords[i]);
                    System.out.println("Mdp Hashé: " + fromByteToHex(md5(wider((passwords[i]+salage[i]).getBytes())))); 
                    System.out.println("Sel : " + salage[i] + "\n");
                }
                
                try (PrintWriter pWriter = new PrintWriter(new FileWriter("avec sel.txt", true))) 
                {
                    
                    for (int i = 0; i < 100; i++)
                    {
                    pWriter.println("Id : " + identifiants[i]);
                    pWriter.println("Mdp : " + passwords[i]);
                    pWriter.println("Mdp Hashé: " + fromByteToHex(md5(wider((passwords[i]+salage[i]).getBytes())))); 
                    pWriter.println("Sel : " + salage[i] + "\n");
                    }
                }
                catch (IOException e)
                {
                    System.out.println ("Erreur d'écriture dans le fichier");
                }
                
                pause();
                main(args);
                
                break;
            case "5" : 
                System.out.println(" Veuillez saisir le message à hasher :");
                toEncode = scan.nextLine();
                System.out.println(" Veuillez saisir la clé :");
                myKey = scan.nextLine();
                
                String salage1 = generate((int)Math.floor(Math.random() * 10 + 1));
                
                System.out.println(" Message : " + toEncode);
                System.out.println("\n --- MD5 généré par notre programme --- \n");
                System.out.println(fromByteToHex(md5(wider(toEncode.getBytes()))));

                System.out.println("\nMdp Hashé avec sel: \n" + fromByteToHex(md5(wider((toEncode+salage1).getBytes()))) + "\n"); 
                System.out.println("Sel : \n" + salage1 + "\n");
                
                System.out.println("Hmac: " + hmac(toEncode+salage1, myKey));
                pause();
                main(args);
                
                break;
            case "6" : break;
         }
    }
}