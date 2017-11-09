# txt2xmi

This program converts tokenized and sentence splitted plain text files into Apache UIMA.

## Usage

On the command line
```bash
java -jar txt2xmi.jar --input INPUT_DIRECTORY --output OUTPUT_DIRECTORY --dictionaryDirectory DICT_DIRECTORY
```

Please not that both input and output options are *directories*, not files. Also, we only read `.txt`-files from the input directory. The output directory should exist, and will (after successful run) contain `.xmi`-files and a file called `typesystem.xml`. 

**DICT_DIRECTORY**: Files in this this directory that end on `txt` are considered to be dictionaries. The filename will be used as class/layer name for the annotations. The file should contain UTF-8 encoded plain texts, with one entry on each line. An entry is annotated as is, i.e., with the exact casing etc. Multiple tokens can be included, but the type and number of spaces between them need to fit the text.

## Compilation

```bash
mvn package
```