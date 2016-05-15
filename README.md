# LZW_Compression
LZW is a lossless compression/decompression algorithm.

The compression algorithm takes a text file as input, encodes the contents of the input file using LZW compression algorithm, 
into UTF-16BE format & writes it into a file with .lzw extension. The contents of this file are not human readable.

The decompression algorithm takes a file with .lzw extension, decodes the contents of the file using LZW decompression algorithm 
and writes it into a text file. This contents of this file are human-readable.
			

Files:
	1. encoder.java  --> To encode a file
	2. decoder.java  --> To decode a file
	
The compression & decompression algorithms can effectively encode & decode files upto 16MB in less than 5 minutes.

How to Run:

1. ENCODING

	a) Run the file encoder.java from command prompt with 2 command line arguments - input_file_name.txt, number_of_bits:
	
		javac encoder.java
		
		java encoder input_file.txt n

	b) The contents of input_file is encoded & written into input_file_name.lzw file

2. DECODING

	a) Run the file decoder.java from command prompt with 2 command line arguments - input_file_name.lzw, number_of_bits:
	
		javac decoder.java
		
		java decoder input_file.lzw n

	b) The contents of input_file is decoded & written into input_file_name_decoded.txt
