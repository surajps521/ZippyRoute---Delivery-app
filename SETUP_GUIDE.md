# ZippyRoute - Quick Setup Guide

This guide will help you get the ZippyRoute Food Delivery App running in minutes.

## Prerequisites

Ensure you have installed:
- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 14+** ([Download](https://www.postgresql.org/download/))
- **Git** (optional)

## Step-by-Step Setup

### Step 1: Setup PostgreSQL Database (5 minutes)

#### On Windows:
1. Open pgAdmin or PostgreSQL Command Line
2. Run the following SQL:
```sql
CREATE DATABASE zippyroute_db;
```

#### On macOS/Linux:
```bash
psql -U postgres
CREATE DATABASE zippyroute_db;
\q
```

### Step 2: Start the Backend (5 minutes)

1. Open terminal/command prompt
2. Navigate to the backend folder:
```bash
cd demo
```

3. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

Wait until you see:
```
ZippyRouteApplication : Started ZippyRouteApplication
```

✅ Backend is now running on `http://localhost:8080/api`

### Step 3: Start the Frontend (2 minutes)

#### Option A: Using Python (Easy)
```bash
cd frontend
python -m http.server 8000
```

#### Option B: Using Node.js
```bash
cd frontend
npx http-server
```

✅ Frontend is now running on `http://localhost:8000` (or the port shown)

### Step 4: Initialize Sample Data (2 minutes)

#### Option A: Windows (Easiest)
```bash
initialize-data.bat
```

#### Option B: macOS/Linux
```bash
bash initialize-data.sh
```

#### Option C: Manual API Calls
Use Postman or curl to create menu items using the `/menu/create` endpoint.

## Quick Test

### 1. Open Browser
```
http://localhost:8000
```

### 2. Register New Account
- Click "Register"
- Fill in details:
  - Full Name: Test User
  - Email: test@example.com
  - Password: Test@123
  - Phone: 9876543210
- Click "Register"

### 3. Browse Menu
- You should see food items
- Try searching and filtering

### 4. Place Order
- Add items to cart
- Click "Proceed to Checkout"
- Enter delivery address
- Click "Place Order"

### 5. Track Order
- Go to "My Orders"
- Click "Track Order"
- See order status

## File Structure

```
ZippyRoute/
├── demo/                    # Backend (Spring Boot)
├── frontend/                # Frontend (HTML/CSS/JS)
├── README.md               # Full documentation
├── SETUP_GUIDE.md          # This file
├── database-setup.sql      # Database schema
├── initialize-data.sh      # Linux/Mac data initialization
└── initialize-data.bat     # Windows data initialization
```

## Configuration Changes (Optional)

### Change Backend Port
Edit `demo/src/main/resources/application.properties`:
```properties
server.port=8080  # Change to your desired port
```

### Change Database Credentials
Edit `demo/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/zippyroute_db
spring.datasource.username=postgres  # Your PostgreSQL username
spring.datasource.password=postgres  # Your PostgreSQL password
```

### Change Frontend API URL
Edit `frontend/js/app.js`:
```javascript
const API_BASE_URL = 'http://localhost:8080/api'; // Change if backend running on different port
```

## Troubleshooting

### Backend won't start
```
Error: org.postgresql.util.PSQLException: Connection refused
```
**Solution**: Make sure PostgreSQL is running
- Windows: Check Services (postgresql-x64-14)
- macOS: `brew services start postgresql`
- Linux: `sudo systemctl start postgresql`

### "Database does not exist"
```
org.postgresql.util.PSQLException: ERROR: database "zippyroute_db" does not exist
```
**Solution**: Create the database using Step 1 instructions

### Frontend shows blank page
```
CORS error in console
```
**Solution**: Update `API_BASE_URL` in `frontend/js/app.js` to match your backend URL

### Port already in use
```
Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
```
**Solution**: Change port in `application.properties`:
```properties
server.port=8081  # Use different port
```

### "Cannot find module" error
```
ModuleNotFoundError: No module named 'http.server'
```
**Solution**: 
- Use Node.js: `npx http-server`
- Or use Python 3: `python3 -m http.server 8000`

## API Documentation

### Authentication

**Register**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "Password123",
    "phoneNumber": "9876543210"
  }'
```

**Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password123"
  }'
```

### Get Menu
```bash
curl http://localhost:8080/api/menu/available \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Create Order
```bash
curl -X POST http://localhost:8080/api/orders/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "deliveryAddress": "123 Main St",
    "phoneNumber": "9876543210",
    "notes": "Ring bell twice",
    "items": [
      {
        "menuItemId": 1,
        "quantity": 2
      }
    ]
  }'
```

## Performance Tips

1. **Clear Browser Cache**
   - Press F12 → Network → Disable Cache
   - Or use `Ctrl+Shift+Delete`

2. **Check Console for Errors**
   - Press F12 to open Developer Tools
   - Check Console tab for any errors

3. **Restart Services if Issues**
   - Restart PostgreSQL
   - Kill and restart backend (`Ctrl+C` then run again)
   - Refresh browser

## Next Steps

1. **Add More Menu Items**
   - Use the API or admin dashboard

2. **Deploy to Cloud**
   - See README.md for deployment guide

3. **Customize Frontend**
   - Edit `frontend/styles/style.css` for colors
   - Edit `frontend/index.html` for layout

4. **Add New Features**
   - Real-time notifications
   - Payment integration
   - Delivery tracking map

## Getting Help

- Check the main [README.md](README.md) for full documentation
- Review backend logs in terminal
- Check browser console (F12) for frontend errors
- Review API responses in Network tab (F12)

## Common Commands

```bash
# Start only backend
cd demo && mvn spring-boot:run

# Start only frontend (Python)
cd frontend && python -m http.server 8000

# Start only frontend (Node)
cd frontend && npx http-server

# Initialize sample data
./initialize-data.sh  # macOS/Linux
initialize-data.bat  # Windows

# Stop services
Ctrl+C  # In terminal
```

---

**Congratulations! 🎉 Your ZippyRoute app is ready to use!**

For issues: Check main README.md or contact support
