Wardrobe Management

At present the application has the following primary end points:
curl and browser ways of hitting these apis:

GET /clothe

curl --location --request GET '127.0.0.1:9000/clothe?name=<cloth Name>'

POST /clothe

curl --location --request POST '127.0.0.1:9000/clothe?name=<cloth Name>&category=<category>'

POST /clothes

Instructions:

Open a web browser and hit the locally running server at [http://localhost:9000]

Attach the csv file to upload

hit the submit button

POST /outfit

curl --location --request POST '127.0.0.1:9000/outfit?name=<outfit name>&tag=<cloth Name>'

Libraries:

scalatestplus-play: 5.1.0
play-json: 2.9.3,
jdbc,
play-slick: 5.0.2,
play-slick-evolutions: 5.0.2,
postgresql: 9.4.1208-jdbc42-atlassian-hosted,
scala-csv: 1.3.10

Required Installments:

Java: 1.8.0_X

postgres: 14.5

Postgres setup:
Install postgress: https://www.postgresql.org/download/
once postgres is installed, postgres server can be started using

brew services start postgresql [For other os please see here: https://www.postgresql.org/docs/ ]

brew services stop postgresql

brew services restart postgresql

By default postgres runs on port 5432.

Instructions:

add db postgres

add user postgres

add password postgres

create table:

create table "clothes" (id serial primary key, name varchar, category varchar);

create table "outfits" (id serial primary key,name varchar,outfit_tag varchar);

Following commands might be useful [postgres]:

psql -U postgres

ALTER USER postgres PASSWORD 'postgres';

select * from clothes;

select * from outfits;

Truncate table clothes;

References:

https://index.scala-lang.org/playframework/play-slick

https://index.scala-lang.org/playframework/play-json

https://mvnrepository.com/artifact/com.typesafe.play/play-slick-evolutions

https://index.scala-lang.org/tototoshi/scala-csv

https://www.playframework.com/documentation/2.8.x/ScalaFileUpload

https://github.com/tototoshi/scala-csv

https://github.com/playframework/play-scala-slick-example

https://github.com/playframework/play-scala-isolated-slick-example/
















