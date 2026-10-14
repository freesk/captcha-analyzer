# Captcha Analyzer

Reads numeric CAPTCHAs using image cleanup and OCR.

The program uses [ocrad](https://www.gnu.org/software/ocrad/) to read a number-based CAPTCHA image with a specific layout.

![CAPTCHA layout](https://i.imgur.com/JCICoqn.png)

The image is split into pieces, and each piece is cleaned of lines and noise. Next, ocrad analyzes each piece. Finally, the program prints the result to the console.

## Requirements

To run the jar file, you need to install [ocrad](https://www.gnu.org/software/ocrad/) and [ImageMagick](https://www.imagemagick.org/script/index.php):

```
brew install ocrad imagemagick
```

Alternatively, you can download the ocrad [source](http://download.savannah.gnu.org/releases/ocrad/) and compile it yourself.

Tested on macOS Sierra only.

## Example

<img src="https://i.imgur.com/vTcbyI1.jpg" width="490">
