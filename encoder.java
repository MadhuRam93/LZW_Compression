/**
 *
 * @author madhu ramachandra
 * File: encoder.java
 */
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.Math.pow;
import java.util.Scanner;

public class encoder {

    private static int num_bits;
    private static File file;
    
    private static int table_size;
    
    private static String[] table;    
    private static int input_counter;
    
    private static String str;
    
    private static int output_code;    
	
    public encoder(int input_bits, String input_file){                   //class constructor
        
        num_bits = input_bits;
        file = new File(input_file);
        
        table_size = (int) pow(2, num_bits);
        
        table = new String[table_size];
        input_counter = 0;
        
        str = "";
        
        output_code = 0;        
    }
    
    public static void create_table() {                                 //table to store all ASCII characters
        for(input_counter=0; input_counter<256; input_counter++){   
            char c = (char)input_counter;
            table[input_counter] = Character.toString(c);
            
        }
    }
    
    public static int get_code(String s){                               //returns the code of the corresponding input String s
                                                                        //by looking up in the table
        for(int i=0; i<input_counter; i++){
            if(table[i].equals(s))
                return i;
        }
        return 0;
    }
    
    public static void compress(){                                      //encodes the input
	
	create_table();                                              		//table is created
        
        String filename = file.getName().substring(0, file.getName().length() - 4);     
        File output_file = new File(filename+".lzw");                               //creating the output file with ".lzw" extension        
           
    try {
        
            Writer w = new OutputStreamWriter(new FileOutputStream(output_file), "UTF-16BE");   //writing to the file in UTF-16BE encoded format

            FileInputStream read_char = new FileInputStream(file);
            char symbol;
            String sym;
            String temp_str = "";
            
            while(read_char.available() > 0) {                          //as long as there are characters present in the file
                symbol = (char) read_char.read();                       //reading next input symbol
                
                sym = Character.toString(symbol);
                
                temp_str = str + sym;
                
                boolean flag = false;
                
                int k = get_code(temp_str);
                if(k != 0){                                             //str+symbol is in table
                    str = temp_str;
                    flag = true;
                }

                if(!flag){                                               //str+symbol is not present in table
                    
                    k = get_code(str);  
                    if(k != 0){
						
                        output_code = k;    							
                        short j = (short)output_code;
                        w.write(j);										//writing encoded value to file

                    }
                    
                    if(input_counter < table_size)
                        table[input_counter++] = temp_str;              //storing the str+sym in the table
                                
                    str = sym;
                    
                }
            }
            
            int k = get_code(str);
            if(k != 0) {
				
                    output_code = k;
                    short j = (short)output_code;
                    w.write(j);
                
            }
            
            w.close();       
            System.out.println("Input file " + file.getName() + " encoded successfully");
               
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
   public static void main(String[] args) {								//main function
        
        int input_bits = Integer.parseInt(args[1]);
        
        if(input_bits >= 9 && input_bits <= 12){
                       
            String input_file = args[0];
            
            File file = new File(input_file);

            if (!file.exists()) {
            System.out.println(file.getName() + " does not exist");
            }
        
            else if (!(file.isFile() && file.canRead())) {
                System.out.println("Cannot read file " + file.getName());
            }
           
            else if(file.getName().contains(".txt")){
                encoder obj = new encoder(input_bits, input_file);
                obj.compress();										//calling compress() to encode the input file
            }
           
            else {
                System.out.println("Please enter a file with '.txt' extension");
            }
        }
        
        else {
            System.out.println("Error: bit length is not in the range of 9 to 12 ");
        }
    }
}