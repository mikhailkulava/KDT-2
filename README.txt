go to $JAVA_HOME directory/jre/lib/
open file "logging.properties";
paste 

java.util.logging.SimpleFormatter.format=[%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS] %4$s %5$s%6$s%n


to section: # default file output is in user's home directory.
