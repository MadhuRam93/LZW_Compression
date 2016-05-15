/**
 *
 * @author madhu ramachandra
 * File: decoder.java
 */
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import static java.lang.Math.pow;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class decoder {
    
    private static int num_bits;
    private static File file;
    
    private static int table_size;
    
    private static String[] table;    
    private static int input_counter;
    
    private static String str;
    private static String new_str;
    private static String prev_str;
    
    private static String output_code;
    
	
    public decoder(int input_bits, String input_file){                      //class constructor
        
        num_bits = input_bits;
        file = new File(input_file);
        
        table_size = (int) pow(2, num_bits);
        
        table = new String[table_size];
        input_counter = 0;
        
        str = "";
        new_str = "";
        prev_str = "";
        
        output_code = "";        
    }
    
    private static void create_table() {                                    //table to store all ASCII characters
        for(input_counter=0; input_counter<256; input_counter++){
            char c = (char)input_counter;
            table[input_counter] = Character.toString(c);
        }
    }
    
    public static String get_string(int c){                              //returns the String of the corresponding input code c
                                                                        //by looking up in the table
        for(int i=0; i<input_counter; i++){
            if(i == c)
                return table[i];
        }
        return null;
    }
    
    private static void decompress(){                                   //decodes the input
        
        create_table();                                                 //table is created
        
        String filename = file.getName().substring(0, file.getName().length() - 4);
        File output_file = new File(filename+"_decoded.txt");       //creating the output file with ".txt" extension
        
        try {
                FileWriter fw = new FileWriter(output_file);                //writing the decoded characters to the file

                Reader read_code = new InputStreamReader(new FileInputStream(file), "UTF-16BE");
                int code;

                code = read_code.read();

                str = get_string(code);

                output_code = str;
                fw.append(output_code);

                code = read_code.read();                                //reading next input 
                while(code != -1) {                                     //as long as there are characters present in the file
                           
                    if(get_string(code) == null) {                      //new string is not present in the table
                        String sym = str.substring(0, 1);
                        new_str = str + sym;
                    }
                    
                    else {                                              //new string is present in the table
                        new_str = get_string(code);
                    }
            
                    output_code = new_str;
                    fw.append(output_code);								//appending the decoded string to the output file
					
                    if(input_counter < table_size) {
                        String sym = new_str.substring(0, 1);           //concatenating previous string & the first character of the new string
                                                                        // and adding it to the table
                        table[input_counter++] = str + sym;
                    }
                    
                    str = new_str;
           
                    code = read_code.read();                            //reading next input code
            
                }
				
                fw.close();
                System.out.println("Input file " + file.getName() + " decoded successfully");
                    
		} catch (FileNotFoundException ex) {
			Logger.getLogger(decoder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
			Logger.getLogger(decoder.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
        
    
    public static void main(String[] args) {						//main function
                               
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
				
				else if(file.getName().contains(".lzw")){
					decoder obj = new decoder(input_bits, input_file);		
					obj.decompress();					//calling decompress() to decode the input file
				}
			   
				else {
					System.out.println("Please enter a file with '.lzw' extension");
				}
			}
        
			else {
				System.out.println("Error: bit length is not in the range of 9 to 12 ");
			}
    }   
}
