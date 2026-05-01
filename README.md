# Setly - Splitwise Clone Backend

A comprehensive expense splitting application built with Spring Boot that allows groups to track shared expenses and settle debts efficiently.

## Features

### User Management
- User registration and authentication with JWT tokens
- User profile management
- Email-based login system
- Password encryption with BCrypt

### Group Management
- Create expense groups (trip, household, friends, custom)
- Add and remove group members
- List group members with their roles
- Multiple currency support per group
- Flexible group types

### Expense Management
- Create expenses with flexible split types (equal, itemwise, percentage)
- Update existing expenses
- Delete expenses
- Track who paid and who owes
- Itemized expense splits
- Expense history and details

### Balance & Settlement
- Real-time balance calculation for group members
- Settlement suggestions using minimum transaction algorithm
- Record payment settlements
- View settlement history
- Track payment methods

## Technology Stack

- **Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Database**: PostgreSQL
- **Authentication**: JWT (JJWT)
- **Build Tool**: Maven
- **ORM**: Hibernate JPA
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Validation**: Jakarta Validation
- **Database Migration**: Flyway

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6.0 or higher

## Installation & Setup

### 1. Database Setup

```sql
CREATE DATABASE setly_db;
```

Update `src/main/resources/application.yaml` with your PostgreSQL credentials:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/setly_db
    username: postgres
    password: your_password
```

### 2. Build the Application

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8088`

### 4. Access API Documentation

Visit `http://localhost:8088/swagger-ui.html` for interactive API documentation

## API Endpoints

### User APIs (`/api/v1/users`)

#### Register User
```
POST /api/v1/users/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "displayName": "John Doe",
  "defaultCurrency": "USD",
  "locale": "en_US",
  "countryCode": "US"
}

Response: LoginResponse with JWT token
```

#### Login User
```
POST /api/v1/users/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: LoginResponse with JWT token
```

#### Get User by ID
```
GET /api/v1/users/{userId}
Authorization: Bearer {token}
```

#### Get All Users
```
GET /api/v1/users
Authorization: Bearer {token}
```

### Group APIs (`/api/v1/groups`)

#### Create Group
```
POST /api/v1/groups
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Trip to Paris",
  "description": "Summer vacation",
  "groupType": "TRIP",
  "baseCurrency": "EUR",
  "createdByUserId": 1
}

Response: GroupResponse
```

#### Add Member to Group
```
POST /api/v1/groups/{groupId}/members
Authorization: Bearer {token}
Content-Type: application/json

{
  "userId": 2,
  "role": "MEMBER"
}
```

#### Get Group Members
```
GET /api/v1/groups/{groupId}/members
Authorization: Bearer {token}

Response: List<GroupMemberResponse>
```

#### Remove Member from Group
```
DELETE /api/v1/groups/{groupId}/members/{userId}
Authorization: Bearer {token}
```

#### Get Group Details
```
GET /api/v1/groups/{groupId}
Authorization: Bearer {token}

Response: GroupResponse
```

#### Get All Groups
```
GET /api/v1/groups
Authorization: Bearer {token}

Response: List<GroupResponse>
```

### Expense APIs (`/api/v1/groups/{groupId}/expenses`)

#### Create Expense
```
POST /api/v1/groups/{groupId}/expenses
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Restaurant Bill",
  "description": "Dinner at Italian restaurant",
  "paidByUserId": 1,
  "createdByUserId": 1,
  "amount": 100.00,
  "currency": "USD",
  "expenseDate": "2024-05-01",
  "splitType": "EQUAL",
  "participants": [
    {
      "userId": 1,
      "owedAmount": 50.00
    },
    {
      "userId": 2,
      "owedAmount": 50.00
    }
  ]
}

Response: ExpenseResponse
```

#### Update Expense
```
PUT /api/v1/groups/{groupId}/expenses/{expenseId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Updated Restaurant Bill",
  "amount": 110.00,
  "participants": [...]
}
```

#### Delete Expense
```
DELETE /api/v1/groups/{groupId}/expenses/{expenseId}
Authorization: Bearer {token}
```

#### Get All Expenses
```
GET /api/v1/groups/{groupId}/expenses
Authorization: Bearer {token}

Response: List<ExpenseResponse>
```

#### Get Expense Details
```
GET /api/v1/groups/{groupId}/expenses/{expenseId}
Authorization: Bearer {token}

Response: ExpenseResponse
```

### Balance APIs (`/api/v1/groups/{groupId}/balances`)

#### Get Group Balances
```
GET /api/v1/groups/{groupId}/balances
Authorization: Bearer {token}

Response: List<UserBalanceResponse>
```

### Settlement APIs (`/api/v1/groups/{groupId}/settlements`)

#### Create Settlement
```
POST /api/v1/groups/{groupId}/settlements
Authorization: Bearer {token}
Content-Type: application/json

{
  "fromUserId": 2,
  "toUserId": 1,
  "amount": 50.00,
  "currency": "USD",
  "paymentMethod": "BANK_TRANSFER",
  "settlementDate": "2024-05-01"
}

Response: SettlementResponse
```

#### Get Settlement Suggestions
```
GET /api/v1/groups/{groupId}/settlements/suggestions
Authorization: Bearer {token}

Response: List<SettlementSuggestion>
```

#### Get Settlements
```
GET /api/v1/groups/{groupId}/settlements
Authorization: Bearer {token}

Response: List<SettlementResponse>
```

## Authentication

All protected endpoints require a JWT token in the `Authorization` header:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

The token is obtained from login or registration endpoints.

## Database Schema

### Users Table
- id (PK)
- email (UNIQUE)
- password_hash
- display_name
- default_currency
- locale
- country_code
- status
- created_at
- updated_at

### Expense Groups Table
- id (PK)
- name
- description
- group_type
- base_currency
- created_by (FK: users)
- created_at
- updated_at

### Group Members Table
- id (PK)
- group_id (FK: expense_groups)
- user_id (FK: users)
- role
- status
- joined_at

### Expenses Table
- id (PK)
- group_id (FK: expense_groups)
- title
- description
- paid_by (FK: users)
- created_by (FK: users)
- amount
- currency
- expense_date
- split_type
- created_at
- updated_at

### Expense Splits Table
- id (PK)
- expense_id (FK: expenses)
- user_id (FK: users)
- owed_amount

### Settlements Table
- id (PK)
- group_id (FK: expense_groups)
- from_user_id (FK: users)
- to_user_id (FK: users)
- amount
- currency
- payment_method
- status
- settlement_date
- created_at

## Split Types

- **EQUAL**: Split expense equally among all participants
- **ITEMWISE**: Specify exact amount each person owes
- **PERCENTAGE**: Split by percentage (not yet implemented in UI)

## Group Types

- **TRIP**: For group trips and vacations
- **HOUSEHOLD**: For shared household expenses
- **FRIENDS**: For general friend group expenses
- **CUSTOM**: For any other grouping

## Member Roles

- **OWNER**: Full permissions, can delete group
- **MEMBER**: Can add expenses and settle debts

## Settlement Suggestions Algorithm

The application uses a greedy algorithm to minimize the number of transactions required to settle all debts:

1. Calculate net balance for each user
2. Separate users into debtors (negative balance) and creditors (positive balance)
3. Match debtors with creditors to minimize transaction count
4. Generate settlement suggestions with optimal amounts

Example:
- User A owes $100
- User B owes $50
- User C is owed $150

Suggested settlements:
- A pays C $100
- B pays C $50

Instead of: A→B, A→C, B→C (3 transactions)

## Error Handling

The application provides consistent error responses:

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-05-01T10:30:00Z"
}
```

Common error codes:
- 400: Bad Request (validation error)
- 401: Unauthorized (authentication required)
- 403: Forbidden (insufficient permissions)
- 404: Not Found (resource doesn't exist)
- 500: Internal Server Error

## Configuration

### JWT Configuration

Update `app.jwt` properties in `application.yaml`:
```yaml
app:
  jwt:
    secret: your-secret-key-here
    expiration: 86400000  # 24 hours in milliseconds
```

### Database Configuration

Update `spring.datasource` properties in `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/setly_db
    username: postgres
    password: your_password
    
  jpa:
    hibernate:
      ddl-auto: update  # or 'validate' in production
```

## Development

### Project Structure

```
src/main/java/com/kt/setly/
├── SetlyApplication.java          # Main application class
├── balance/                       # Balance calculation module
├── common/                        # Common utilities and exceptions
│   ├── exception/
│   └── response/
├── config/                        # Application configuration
├── expense/                       # Expense management module
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── group/                         # Group management module
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── health/                        # Health check endpoints
├── settlement/                    # Settlement module
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── security/                      # JWT security
└── user/                          # User management module
    ├── controller/
    ├── dto/
    ├── entity/
    ├── repository/
    └── service/
```

### Building

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Running with IDE

Run the `SetlyApplication.java` main method directly in your IDE.

## Future Enhancements

- [ ] Real-time expense sync with WebSockets
- [ ] Mobile app integration
- [ ] Email notifications
- [ ] Recurring expenses
- [ ] Expense categories and tags
- [ ] Receipt image upload and OCR
- [ ] User search functionality
- [ ] Friend requests system
- [ ] Activity feed/history
- [ ] Advanced reporting and analytics
- [ ] Multi-currency conversion
- [ ] Scheduled payments
- [ ] Expense history and undo functionality

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support, email support@setly.com or open an issue in the repository.

## Changelog

### Version 0.0.1 (Initial Release)
- User registration and authentication with JWT
- Group management (create, add members, remove members)
- Expense tracking with multiple split types
- Balance calculation
- Settlement tracking and suggestions
- Comprehensive API documentation with Swagger

