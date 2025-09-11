# Courses alkalmazás

## Funkcionalitás

* Kurzust lehet meghirdetni, kóddal és névvel (announcement)
* Kurzusra lehet jelentkezni (enrollment)

## Adatbázis Docker használatával

```shell
docker run -d -e POSTGRES_DB=courses -e POSTGRES_USER=courses -e POSTGRES_PASSWORD=courses -p 5432:5432 --name courses-postgres postgres
```