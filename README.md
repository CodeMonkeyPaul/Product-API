<h1>Assessment</h1>

- When running in IntellIJ make sure to install the Lombok plugin and enable Lombok annotation processing.
- Visit database console for local setup at: http://localhost:8080/h2-console (username: sa, password empty)

<h3>GET request</h3>
```GET localhost:8080/product```

- Will provide a list of all products within the database.
- Can be amended with the (optional) query parameter: _search_
    - ```GET localhost:8080/product?search=city```

<h3>POST / PUT requests</h3>
```POST or PUT localhost:8080/product```

- Requires a request body such as :

```
{
  "id": 100,
  "name": "Hotel California",
  "category": "Hotel",
  "location": "California",
  "price": 1234.00,
  "currency": "USD",
  "description": "Some better description then ChatGPT did."
}
```

- Will validate received Product object.

<h3>Randomize discounts</h3>
```PUT localhost:8080/admin/randomise-discounts```

- Mimic external changes being made to the products in this database. In this dataset that might be third parties
  updating their offered products by regularly discounting them. 