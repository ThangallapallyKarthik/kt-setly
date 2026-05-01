# Setly - Quick Start Guide

Get started with Setly in 5 minutes! 🚀

---

## Prerequisites Check

Make sure you have:
- ✅ Java 17+ installed
- ✅ PostgreSQL running
- ✅ Database `setly_db` created

---

## 1. Configure Database (30 seconds)

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    password: YOUR_POSTGRES_PASSWORD  # Change this!
```

---

## 2. Start Application (1 minute)

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run
```

Wait for: `Started SetlyApplication in X.XXX seconds`

---

## 3. Open Swagger UI (30 seconds)

Visit in your browser:
```
http://localhost:8088/swagger-ui.html
```

You'll see all API endpoints documented and testable!

---

## 4. Test Quick Workflow (3 minutes)

### Step 1: Register User
```bash
POST http://localhost:8088/api/v1/users/register
{
  "email": "john@example.com",
  "password": "password123",
  "displayName": "John Doe"
}
```

**Response:** Get JWT token from response

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### Step 2: Create Group
```bash
POST http://localhost:8088/api/v1/groups
Authorization: Bearer YOUR_TOKEN
{
  "name": "Weekend Trip",
  "groupType": "TRIP",
  "baseCurrency": "USD",
  "createdByUserId": 1
}
```

### Step 3: Create Expense
```bash
POST http://localhost:8088/api/v1/groups/1/expenses
Authorization: Bearer YOUR_TOKEN
{
  "title": "Restaurant",
  "paidByUserId": 1,
  "createdByUserId": 1,
  "amount": 100,
  "currency": "USD",
  "expenseDate": "2024-05-01",
  "splitType": "EQUAL",
  "participants": [
    {"userId": 1, "owedAmount": 50},
    {"userId": 2, "owedAmount": 50}
  ]
}
```

### Step 4: Check Balances
```bash
GET http://localhost:8088/api/v1/groups/1/balances
Authorization: Bearer YOUR_TOKEN
```

### Step 5: Get Settlement Suggestions
```bash
GET http://localhost:8088/api/v1/groups/1/settlements/suggestions
Authorization: Bearer YOUR_TOKEN
```

---

## Using REST Client File

Use the included `API_TESTING.http` file with:
- **VS Code**: Install "REST Client" extension, click "Send Request"
- **Postman**: Import the file
- **Insomnia**: Import the file

---

## Key Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/users/register` | Create account |
| POST | `/api/v1/users/login` | Get JWT token |
| POST | `/api/v1/groups` | Create expense group |
| POST | `/api/v1/groups/{id}/members` | Add person to group |
| POST | `/api/v1/groups/{id}/expenses` | Record expense |
| GET | `/api/v1/groups/{id}/balances` | See who owes whom |
| GET | `/api/v1/groups/{id}/settlements/suggestions` | Get payment suggestions |

---

## Common Workflows

### Track Trip Expenses
1. Create group
2. Add friends as members
3. Create expenses as people pay
4. Check balances
5. Use settlement suggestions
6. Record payments

### Share Household Bills
1. Create "HOUSEHOLD" group
2. Add roommates
3. Add monthly bills
4. Track balances over time
5. Settle monthly

### Split Restaurant Bill
1. Create "FRIENDS" group
2. Add friends
3. Add bill as expense
4. Use "ITEMWISE" split if items differ
5. See settlement

---

## Important Notes

⚠️ **Default JWT Secret**
- The JWT secret in `application.yaml` is for development only
- Change it before deploying to production!

⚠️ **Bearer Token Format**
- All protected endpoints need: `Authorization: Bearer <token>`
- Token expires after 24 hours
- Token expires after 24 hours, need to re-login

⚠️ **Split Totals**
- Participant amounts must exactly equal expense amount
- ✅ Good: $100 expense with $50 + $50 splits
- ❌ Bad: $100 expense with $50 + $40 splits

---

## Troubleshooting

### Can't connect to database?
```
Check if PostgreSQL is running
Verify password in application.yaml
Verify database "setly_db" exists
```

### Port 8088 already in use?
```
Change port in application.yaml:
  server:
    port: 8089
```

### JWT token expired?
```
Register or login again to get new token
```

### Split amount validation error?
```
Make sure participant amounts add up exactly to expense amount
```

---

## Full Documentation

📖 See `README.md` for comprehensive documentation
🔧 See `SETUP.md` for detailed setup instructions
📋 See `COMPLETION_CHECKLIST.md` for features list

---

## Next Steps

1. ✅ Start the application
2. ✅ Test basic workflow above
3. ✅ Read `README.md` for all features
4. ✅ Explore Swagger UI at `/swagger-ui.html`
5. ✅ Build your frontend!

---

Happy expense tracking! 💰

Questions? Check:
- `README.md` - Comprehensive guide
- `SETUP.md` - Installation help
- `API_TESTING.http` - Example requests
- Swagger UI at `http://localhost:8088/swagger-ui.html`

