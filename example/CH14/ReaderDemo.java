package onlyfun.caterpillar;
 
import java.io.*; 
 
public class ReaderDemo {
    public static void main(String[] args) {
        try { 
            PushbackInputStream pushbackInputStream = 
                new PushbackInputStream( 
                         new FileInputStream(args[0])); 
            byte[] array = new byte[2]; 

            ByteArrayInputStream byteArrayStream = 
                new ByteArrayInputStream(array);

            // reader會從已讀的位元陣列中取出資料
            InputStreamReader reader = 
                new InputStreamReader(byteArrayStream); 

            int tmp = 0; 
            int count = 0; 

            while((count = pushbackInputStream.read(array))
                                             != -1) {
                // 兩個位元組轉換為整數 
                tmp = (short)((array[0] << 8) | 
                      (array[1] & 0xff)); 
                tmp = tmp & 0xFFFF; 
 
                // 判斷是否為BIG5，如果是則顯示BIG5中文字
                if(tmp >= 0xA440 && tmp < 0xFFFF) {
                    System.out.println("BIG5: " + 
                                   (char)reader.read()); 
                    // 重置ArrayInputStream的讀取游標
                    // 下次reader才會再重頭讀取資料
                    byteArrayStream.reset(); 
                } 
                else { 
                    // 將第二個位元組推回串流 
                    pushbackInputStream.unread(array, 1, 1); 
                    // 顯示ASCII範圍的字元
                    System.out.println("ASCII: " + 
                            (char)array[0]); 
                } 
            } 
 
            pushbackInputStream.close(); 
        } 
        catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("請指定檔案名稱");
        }
        catch(IOException e) { 
            e.printStackTrace(); 
        } 
    }
}