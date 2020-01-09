# multipartfile-xss-scan
This project provides POC code to scan a `MultipartFile`. This is the type of file included in the arg list within the admin service for the endpoint where the user can upload their logo.

# usable part
The only part of real interest which needs to be copied over to the admin service is the [checkForXss](src/main/java/junk/Main#checkForXss) method.

# build
`mvn clean package`

# run
I've only run this within Eclipse. To run the compiled jar you'd need to construct the class path…

# sample output
```bash
Checking: img/motorcyclist.svg
No malicious code detected
Checking: img/bad.svg
Exception in thread "main" java.lang.Exception: Potential malicious code detected…
	at junk.Main.run(Main.java:38)
	at junk.Main.main(Main.java:28)
```
