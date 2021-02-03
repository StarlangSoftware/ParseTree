For Developers
============

You can also see [Python](https://github.com/starlangsoftware/ParseTree-Py), [C++](https://github.com/starlangsoftware/ParseTree-CPP), or [C#](https://github.com/starlangsoftware/ParseTree-CS) repository.

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called ParseTree will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/ParseTree.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `ParseTree/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 


## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run ParseTree.

**From Console**

Go to `ParseTree` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `ParseTree` root module.

**From Console**

Use below line to generate jar file:

     mvn install

## Maven Usage

        <dependency>
            <groupId>io.github.starlangsoftware</groupId>
            <artifactId>ParseTree</artifactId>
            <version>1.0.2</version>
        </dependency>

Detailed Description
============

+ [TreeBank](#treebank)
+ [ParseTree](#parsetree)

## TreeBank

To load a TreeBank composed of saved ParseTrees from a folder:

	TreeBank(File folder)

To load trees with a specified pattern from a folder of trees: 

	TreeBank(File folder, String pattern)
	
To load trees with a specified pattern and within a specified range of numbers from a folder of trees:

	TreeBank(File folder, String pattern, int from, int to)
	
the line above is used. For example,

	a = TreeBank(new File("/mypath"));

the line below is used to load trees under the folder "mypath" which is under the current folder. If only the trees with ".train" extension under the same folder are to be loaded:

	a = TreeBank(new File("/mypath"), ".train");

If among those trees, only the ones between 1 and 500 are to be loaded:

	a = TreeBank(new File("/mypath"), ".train", 1, 500);

the line below is used. 

To iterate over the trees after the TreeBank is loaded:

	for (int i = 0; i < a.size(); i++){
		ParseTree p = a.get(i);
	}
	
a block of code like this can be useful.

## ParseTree

To load a saved ParseTree:

	ParseTree(FileInputStream file)
	
is used. Usually it is more useful to load a TreeBank as explained above than loading the ParseTree one by one.

To find the node number of a ParseTree:

	int nodeCount()
	
leaf number of a ParseTree:

	int leafCount()
	
number of words in a ParseTree:

	int wordCount(boolean excludeStopWords)
	
above methods can be used.

## Cite
If you use this resource on your research, please cite the following paper: 

```
@inproceedings{yildiz2014constructing,
  title={Constructing a {T}urkish-{E}nglish parallel treebank},
  author={Y{\i}ld{\i}z, O. T. and Solak, E. and G{\"o}rg{\"u}n, O. and Ehsani, R.},
  booktitle={Proceedings of the 52nd Annual Meeting of the Association for Computational Linguistics},
  volume={2},
  pages={112--117},
  year={2014}
}

@incollection{yildiz2015constructing,
  title={Constructing a {T}urkish constituency parse treeBank},
  author={Y{\i}ld{\i}z, O. T. and Solak, E. and {\c{C}}and{\i}r, {\c{S}}. and Ehsani, R. and G{\"o}rg{\"u}n, O.},
  booktitle={Information Sciences and Systems 2015},
  pages={339--347},
  year={2015},
  publisher={Springer}
}

@InProceedings{gorgun16,
  author    = {O. Gorgun and O. T. Yildiz and E. Solak and R. Ehsani},
  title     = {{E}nglish-{T}urkish Parallel Treebank with Morphological Annotations and its Use in Tree-based SMT},
  booktitle = {International Conference on Pattern Recognition and Methods},
  year      = {2016},
  address   = {Rome, Italy},
  pages     = {510--516}
}

