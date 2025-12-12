# Customer API Documentation

## Base URL
`http://localhost:8080/api/customers`

## Endpoints

### 1. Get All Customers
**GET** `/api/customers`

**Response:** 200 OK
```json
[
        {
            "id": 1,
            "customerCode": "C001",
            "fullName": "John Doe",
            "email": "john.doe@example.com",
            "phone": "+1-555-0101",
            "address": "123 Main St, New York, NY 10001",
            "status": "ACTIVE",
            "createdAt": "2025-12-12T05:03:49"
        },
        {
            "id": 2,
            "customerCode": "C002",
            "fullName": "Jane Smith",
            "email": "jane.smith@example.com",
            "phone": "+1-555-0102",
            "address": "456 Oak Ave, Los Angeles, CA 90001",
            "status": "ACTIVE",
            "createdAt": "2025-12-12T05:03:49"
        },
        {
            "id": 3,
            "customerCode": "C003",
            "fullName": "Bob Johnson",
            "email": "bob.johnson@example.com",
            "phone": "+1-555-0103",
            "address": "789 Pine Rd, Chicago, IL 60601",
            "status": "ACTIVE",
            "createdAt": "2025-12-12T05:03:49"
        },
        {
            "id": 4,
            "customerCode": "C004",
            "fullName": "Alice Brown",
            "email": "alice.brown@example.com",
            "phone": "+1-555-0104",
            "address": "321 Elm St, Houston, TX 77001",
            "status": "INACTIVE",
            "createdAt": "2025-12-12T05:03:49"
        },
        {
            "id": 5,
            "customerCode": "C005",
            "fullName": "Charlie Wilson",
            "email": "charlie.wilson@example.com",
            "phone": "+1-555-0105",
            "address": "654 Maple Dr, Phoenix, AZ 85001",
            "status": "ACTIVE",
            "createdAt": "2025-12-12T05:03:49"
        }
    ]

### 2. GET customer by ID
**GET** `/api/customers/1`

**Response:** 200 OK
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-0101",
    "address": "123 Main St, New York, NY 10001",
    "status": "ACTIVE",
    "createdAt": "2025-12-12T05:03:49"
}

**GET** `/api/customers/8`
**Response:** 404 Not Found
```json
{
    "timestamp": "2025-12-11T21:39:39.356637",
    "status": 404,
    "error": "Not Found",
    "message": "Customer not found with id: 8",
    "path": "/api/customers/8",
    "details": null
}



### 3. POST create customer
**POST** `/api/customers`

**body**
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "0772724834",
    "address": "999 Broadway, Seattle, WA 98101"
}

**Response:** 201 CREATED
```json
{
    "id": 6,
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "0772724834",
    "address": "999 Broadway, Seattle, WA 98101",
    "status": "ACTIVE",
    "createdAt": "2025-12-11T22:04:25.4690355"
}

### 4. PUT update customer
**PUT** `/api/customers/7`

**body**
{
    "customerCode": "C006",
    "fullName": "David Miller Jr.",
    "email": "david.miller.jr@example.com",
    "phone": "0772724834",
    "address": "1000 Broadway, Seattle, WA 98101"
}
**Response:** 200 OK
```json
{
    "id": 6,
    "customerCode": "C006",
    "fullName": "David Miller Jr.",
    "email": "david.miller.jr@example.com",
    "phone": "0772624834",
    "address": "1000 Broadway, Seattle, WA 98101",
    "status": "ACTIVE",
    "createdAt": "2025-12-11T22:04:25"
}


### 5. PATCH partial update
**PATCH** `/api/customers/5`
**body**
{
    "fullName": "Charlie Partially Updated"
}
**Response:** 200 OK
```json
{
    "id": 5,
    "customerCode": "C005",
    "fullName": "Charlie Partially Updated",
    "email": "charlie.wilson@example.com",
    "phone": "+1-555-0105",
    "address": "654 Maple Dr, Phoenix, AZ 85001",
    "status": "ACTIVE",
    "createdAt": "2025-12-06T20:35:16"
}

### 6. DELETE customer
**DELETE** `/api/customers/6`
**Response:** 200 OK
```json
{
    "message": "Customer deleted successfully"
}

### 7. Search Customers
**GET** `/api/customers/search?keyword=john`

**Response:** 200 OK
```json
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1-555-0101",
        "address": "123 Main St, New York, NY 10001",
        "status": "ACTIVE",
        "createdAt": "2025-12-12T05:03:49"
    },
    {
        "id": 3,
        "customerCode": "C003",
        "fullName": "Bob Johnson",
        "email": "bob.johnson@example.com",
        "phone": "+1-555-0103",
        "address": "789 Pine Rd, Chicago, IL 60601",
        "status": "ACTIVE",
        "createdAt": "2025-12-12T05:03:49"
    }
]

### 8. Filter By Status
**GET** `/api/customers/status/ACTIVE`

**Response:** 200 OK
```json
[
    {
        "id": 1,
        "customerCode": "C001",
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phone": "+1-555-0101",
        "address": "123 Main St, New York, NY 10001",
        "status": "ACTIVE",
        "createdAt": "2025-12-12T05:03:49"
    },
    {
        "id": 2,
        "customerCode": "C002",
        "fullName": "Jane Smith",
        "email": "jane.smith@example.com",
        "phone": "+1-555-0102",
        "address": "456 Oak Ave, Los Angeles, CA 90001",
        "status": "ACTIVE",
        "createdAt": "2025-12-12T05:03:49"
    },
    {
        "id": 3,
        "customerCode": "C003",
        "fullName": "Bob Johnson",
        "email": "bob.johnson@example.com",
        "phone": "+1-555-0103",
        "address": "789 Pine Rd, Chicago, IL 60601",
        "status": "ACTIVE",
        "createdAt": "2025-12-12T05:03:49"
    },
    {
        "id": 5,
        "customerCode": "C005",
        "fullName": "Charlie Wilson",
        "email": "charlie.wilson@example.com",
        "phone": "+1-555-0105",
        "address": "654 Maple Dr, Phoenix, AZ 85001",
        "status": "ACTIVE",
        "createdAt": "2025-12-12T05:03:49"
    }
]


Document examples for:

- 200 OK
  **GET** `/api/customers/1`
**Response:** 200 OK
```json
{
    "id": 1,
    "customerCode": "C001",
    "fullName": "John Partially Updated",
    "email": "john.updated@example.com",
    "phone": "0772624834",
    "address": "New Address",
    "status": "ACTIVE",
    "createdAt": "2025-12-06T20:35:16"
}

- 201 Created
**POST** `/api/customers`
**body**
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "0772724834",
    "address": "999 Broadway, Seattle, WA 98101"
}
**Response:** 201 CREATED
```json
{
    "id": 7,
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "0772724834",
    "address": "999 Broadway, Seattle, WA 98101",
    "status": "ACTIVE",
    "createdAt": "2025-12-11T21:20:53.1656074"
}

- 400 Bad Request (validation)
**POST** `/api/customers`
**body**
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "abc",
    "address": "999 Broadway, Seattle, WA 98101"
}
**Response:** 201 CREATED
```json
{
    "timestamp": "2025-12-11T21:46:28.8947152",
    "status": 400,
    "error": "Validation Failed",
    "message": "Invalid input data",
    "path": "/api/customers",
    "details": [
        "phone: Invalid phone number format"
    ]
}

- 404 Not Found
**GET** `/api/customers/8`
**Response:** 404 Not Found
```json
{
    "timestamp": "2025-12-11T21:39:39.356637",
    "status": 404,
    "error": "Not Found",
    "message": "Customer not found with id: 8",
    "path": "/api/customers/8",
    "details": null
}

- 409 Conflict (duplicate)
**POST** `/api/customers`

**body**
{
    "customerCode": "C006",
    "fullName": "David Miller",
    "email": "david.miller@example.com",
    "phone": "0772724834",
    "address": "999 Broadway, Seattle, WA 98101"
}
**Response:** 409 Conflict
```json
{
    "timestamp": "2025-12-11T21:51:40.5602412",
    "status": 409,
    "error": "Conflict",
    "message": "Customer code already exists: C006",
    "path": "/api/customers",
    "details": null
}

- 500 Internal Server Error
**GET** `/api`
**Response:** 500 Internal Server Error
```json
{
    "timestamp": "2025-12-11T21:53:50.9201825",
    "status": 500,
    "error": "Internal Server Error",
    "message": "No static resource api.",
    "path": "/api",
    "details": null
}