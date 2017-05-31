# txt2xmi

This program converts tokenized and sentence splitted plain text files into Apache UIMA.

## Usage

On the command line
```bash
java -jar txt2xmi.jar --input INPUT_DIRECTORY --output OUTPUT_DIRECTORY
```

Please not that both input and output options are *directories*, not files. Also, we only read `.txt`-files from the input directory. The output directory should exist, and will (after successful run) contain `.xmi`-files and a file called `typesystem.xml`. 

## Compilation

```bash
mvn package
```