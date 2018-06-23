### Captcha Analyzer

The program uses [ocrad](https://www.gnu.org/software/ocrad/) to analyze a number based captcha image of a certain layout.   
     
![alt tag](https://i.imgur.com/JCICoqn.png)    

The image gets broken into pieces and then every piece gets cleaned from lines and noise. The following step is actual analyzation by [ocrad](https://www.gnu.org/software/ocrad/) of every piece. Finally, the program prints the result into the console.    

To run the jar file you need to install [ocrad](https://www.gnu.org/software/ocrad/) and [imagemagick](https://www.imagemagick.org/script/index.php).     

```
brew install imagemagick
```

To install [ocrad](https://www.gnu.org/software/ocrad/) you need to download its [source](http://download.savannah.gnu.org/releases/ocrad/) and compile it.

Tested on macOS Sierra only.

Example   

<img src="https://i.imgur.com/vTcbyI1.jpg" width="490">
