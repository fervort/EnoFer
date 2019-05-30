# EnoFer
Eclipse Plugin for Enovia / 3DEXPERIENCE Platform 

## Download latest version

[Download Link](https://github.com/fervort/EnoFer/releases/download/v2.0.0.beta/Enofer_1.0.0.beta_binary.zip)

## How to build & deploy the JAR
After downloading the plugin, you have to update `com.fervort.enofer_X.X.X.X.jar` file with the latest version of `eMatrixServletRMI.jar`

Steps :
  - Unzip the downloaded plugin
  - Copy `eMatrixServletRMI.jar` file from your web server into `lib` folder of the unzipped plugin. In case of TomEE, it is located in path `/webapps/internal/WEB-INF/lib` folder
  - Run `buildJAR.bat` script. This script will copy `eMatrixServletRMI.jar` you have just pasted in `lib` folder into the JAR `com.fervort.enofer_X.X.X.X.jar` . If you are sucessful, you will see change in size and timestamp of JAR file.

  - Copy updated JAR `com.fervort.enofer_X.X.X.X.jar` into your Eclipse plugin folder . 
  
  - Restart Eclipse.
