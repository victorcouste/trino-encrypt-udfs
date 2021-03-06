# trino-encrypt-udfs

Example of Trino UDFs Plugin to encrypt and decrypt values with a password.

## Introduction

In [Trino](https://trino.io) you can create new Plugins by implementing interfaces and override methods defined by the [SPI](https://trino.io/docs/current/develop/spi-overview.html).

Plugins can provide additional Connectors, Types, Functions and with this project we implement 2 new [SQL Functions](https://trino.io/docs/current/develop/functions.html) (or UDFs / User-Defined Functions) to encrypt or decrypt a value (from a column or not) with a password.

The method used to encrypt a value is [PBE](http://www.crypto-it.net/eng/theory/pbe.html) (Password Based Encryption), a method where the encryption key (which is binary data) is derived from a password (string). PBE is using an encryption key generated from a password, random salt and number of iterations.
Details on [Java implementation](https://www.javamex.com/tutorials/cryptography/password_based_encryption.shtml), we use the [PBEWithMD5AndDES](https://www.javamex.com/tutorials/cryptography/pbe_key_derivation.shtml) mode.



## Build

### requires
* Java 11
* Maven 4.0.0+ (for building)

```
mvn clean package
```

If you want skip unit tests, please run:
```
mvn clean package -DskipTests
```

It will generate a **trino-encrypt-udfs-{version}.jar** and **trino-encrypt-udfs-{version}** folder in target directory.
   
## Deploy

Copy the **trino-encrypt-udfs-{version}** folder from **target** directory in your Trino **plugin** directory and restart Trino server.
   
```bash
% cp -R ./target/trino-encrypt-udfs-{version} <trino-server-folder>/plugin/trino-encrypt-udfs

% <trino-server-folder>/bin/launcher restart
```

Then you should find 2 new functions **encrypt** and **decrypt** if you list all available functions of your Trino server with **SHOW FUNCTIONS** SQL command:
``` 
"encrypt","varchar","varchar, varchar","scalar","true","UDF to encrypt a value with a given password"

"decrypt","varchar","varchar, varchar","scalar","true","UDF to decrypt a value with a given password"
``` 
## Usage

With a local trino server and trino CLI you can test the UDFs with:
``` 
%<trino-cli-folder>/trino --execute "SELECT encrypt('myvalue','mypassword')"
```

SQL queries to use and test functions:

```
SELECT decrypt(encrypt('myvalue','mypassword'),'mypassword')

SELECT decrypt(encrypt('myvalue','mypassword'),'my_new_password')
```
With last query you must get the message ``"Wrong password for decryption"``.


Tests on a tpch table:
```
SELECT encrypt(name,'new_password') FROM tpch.sf1.region
```
To create a table with encrypted data:

```
CREATE TABLE your_catalog.your_schema.region_encrypt AS SELECT encrypt(name,'new_password') FROM tpch.sf1.region
```

![Trino udfs queries](https://github.com/victorcouste/trino-encrypt-udfs/blob/main/queries.png?raw=true)