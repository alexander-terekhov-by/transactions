### **Transactions**

**Build**

To build and launch project it's required to have JDK 17+ and Docker.

Project initial transaction and balance csv configuration can be found under 'src/main/resources'.

Gradle wrapper can be used instead of locally installed Gradle.
To build the project need to execute: 

`gradle buildEverything`
<br> or `./gradlew buildEverything` (Linux)
<br> or `gradlew.bat buildEverything` (Windows)

It will execute tests and build docker image.

**Run**

To launch docker image need to run:

`docker run --publish 8080:8080 docker.io/teran/by.teran.transactions:1.0`

Application exposes port 8080 and it's available via <br>
`GET http://localhost:8080/transactions/rejected/{username}` request

**Test**

`GET http://localhost:8080/transactions/rejected/testusr1@test.com` 
should return:

```json
{
  "rejectedTransactions": [
    {
      "name": "test",
      "surname": "usr1",
      "email": "testusr1@test.com",
      "amount": 100,
      "id": "TR6"
    },
    {
      "name": "test",
      "surname": "usr1",
      "email": "testusr1@test.com",
      "amount": 100,
      "id": "TR7"
    },
    {
      "name": "test",
      "surname": "usr1",
      "email": "testusr1@test.com",
      "amount": 100,
      "id": "TR8"
    }
  ]
}
```
