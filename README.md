A simple key-value store... Redis-xtralite, if you will.
Made just for fun.

# Trying It Out
- Clone the project.
- Go to the project's folder.
- Run "mvn install".
- Run "java -jar target/simple-store-1.0-SNAPSHOT.jar".

NOTE: Requires Java 1.7.

# Commands
- SET key value
- UNSET key
- GET key
- NUMEQUALTO value
- BEGIN
- ROLLBACK
- COMMIT
- END
- 
# Example
```
java -jar target/simple-store-1.0-SNAPSHOT.jar 
SIMPLE STORE: SET a apple
  * SUCCESS: Set a to apple
SIMPLE STORE: SET b banana
  * SUCCESS: Set b to banana
SIMPLE STORE: SET c cat
  * SUCCESS: Set c to cat
SIMPLE STORE: GET b
  * SUCCESS: Found value 'banana' for key b
  * GOT VALUE: banana
SIMPLE STORE: SET d dog
  * SUCCESS: Set d to dog
SIMPLE STORE: UNSET b
  * SUCCESS: Removed key: b
SIMPLE STORE: GET b
  * SUCCESS: No value found for key b
SIMPLE STORE: NUMEQUALTO dog
  * SUCCESS: Found 1 keys with value 'dog'
  * GOT VALUE: 1
SIMPLE STORE: SET a dog
  * SUCCESS: Set a to dog
SIMPLE STORE: NUMEQUALTO dog
  * SUCCESS: Found 2 keys with value 'dog'
  * GOT VALUE: 2
SIMPLE STORE: BEGIN
  * SUCCESS: Transaction begun.
SIMPLE STORE: SET c dog
  * SUCCESS: Set c to dog
SIMPLE STORE: NUMEQUALTO dog
  * SUCCESS: Found 3 keys with value 'dog'
  * GOT VALUE: 3
SIMPLE STORE: NUMEQUALTO cat
  * SUCCESS: Found 0 keys with value 'cat'
  * GOT VALUE: 0
SIMPLE STORE: ROLLBACK
  * SUCCESS: Transaction rolled back.
SIMPLE STORE: NUMEQUALTO dog
  * SUCCESS: Found 2 keys with value 'dog'
  * GOT VALUE: 2
SIMPLE STORE: NUMEQUALTO cat
  * SUCCESS: Found 1 keys with value 'cat'
  * GOT VALUE: 1
SIMPLE STORE: BEGIN 
  * SUCCESS: Transaction begun.
SIMPLE STORE: SET b bumblebee
  * SUCCESS: Set b to bumblebee
SIMPLE STORE: BEGIN
  * SUCCESS: Transaction begun.
SIMPLE STORE: SET c coyote
  * SUCCESS: Set c to coyote
SIMPLE STORE: GET c
  * SUCCESS: Found value 'coyote' for key c
  * GOT VALUE: coyote
SIMPLE STORE: GET b
  * SUCCESS: Found value 'bumblebee' for key b
  * GOT VALUE: bumblebee
SIMPLE STORE: ROLLBACK
  * SUCCESS: Transaction rolled back.
SIMPLE STORE: GET c
  * SUCCESS: Found value 'cat' for key c
  * GOT VALUE: cat
SIMPLE STORE: GET b
  * SUCCESS: Found value 'bumblebee' for key b
  * GOT VALUE: bumblebee
SIMPLE STORE: COMMIT
  * SUCCESS: Data saved.
SIMPLE STORE: COMMIT
  * ERROR: No transaction. This command requires a transaction.
SIMPLE STORE: END
GOODBYE.
```

# Example Using Input from File
```
-------- > cat testin.txt 
SET a apple
GET a
SET b banana
NUMEQUALTO banana
END

-------- > cat testin.txt | java -jar target/simple-store-1.0-SNAPSHOT.jar 
SIMPLE STORE:   * SUCCESS: Set a to apple
SIMPLE STORE:   * SUCCESS: Found value 'apple' for key a
  * GOT VALUE: apple
SIMPLE STORE:   * SUCCESS: Set b to banana
SIMPLE STORE:   * SUCCESS: Found 1 keys with value 'banana'
  * GOT VALUE: 1
SIMPLE STORE: GOODBYE.
```




