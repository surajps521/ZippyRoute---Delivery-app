# ZippyRoute - Food Delivery Application

A modern food delivery application built with Spring Boot backend and vanilla JavaScript frontend, featuring user authentication, menu browsing, order management, and real-time order tracking.

## Project Structure

```
ZippyRoute/
├── demo/                          # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/zippyroute/demo/
│   │       ├── controller/       # REST API Controllers
│   │       ├── entity/           # JPA Entities
│   │       ├── repository/       # Data Access Layer
│   │       ├── service/          # Business Logic
│   │       ├── util/             # JWT & Password Utilities
│   │       └── ZippyRouteApplication.java
│   ├── src/main/resources/
│   │   └── application.properties # Configuration
│   └── pom.xml                   # Maven Dependencies
│
└── frontend/                      # Frontend Application
    ├── index.html               # Main HTML
    ├── styles/
    │   └── style.css            # Styling
    └── js/
        └── app.js               # Application Logic
```

## Features

### Backend Features
- **User Authentication**: Register and login with JWT token-based authentication
- **Menu Management**: Browse available food items by category
- **Order Management**: Create, view, update, and cancel orders
- **Order Tracking**: Real-time order status tracking with delivery person details
- **Database**: PostgreSQL for data persistence
- **Security**: Password encryption and JWT token validation

### Frontend Features
- **Authentication Page**: User registration and login
- **Menu Page**: Browse all menu items, filter by category, search functionality
- **Shopping Cart**: Add/remove items, manage quantities
- **Checkout**: Delivery address and order notes
- **Order Tracking**: Visual order status timeline with delivery person info
- **Order History**: View all placed orders with status
- **Responsive Design**: Mobile-friendly UI

## Technology Stack

### Backend
- **Framework**: Spring Boot 4.0.3
- **Database**: PostgreSQL 14+
- **Security**: Spring Security, JWT (jjwt 0.12.3)
- **ORM**: Spring Data JPA with Hibernate
- **Language**: Java 17
- **Build Tool**: Maven

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling (Grid, Flexbox)
- **Vanilla JavaScript** - Logic and API calls
- **Local Storage** - Client-side data persistence

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 14+
- Modern web browser

### Backend Setup

1. **Create PostgreSQL Database**
```sql
CREATE DATABASE zippyroute_db;
```

2. **Configure Database Connection**
   - Open `demo/src/main/resources/application.properties`
   - Update these properties if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/zippyroute_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

3. **Build and Run Backend**
```bash
cd demo
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080/api`

4. **Initialize Sample Data** (Optional)
   Create sample menu items via the create menu endpoint:
```bash
curl -X POST http://localhost:8080/api/menu/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Margherita Pizza",
    "description": "Fresh tomato, basil, and cheese",
    "price": 299,
    "category": "Pizza",
    "imageUrl": "pizza.jpg",
    "available": true
  }'
```

### Frontend Setup

1. **Open Frontend**
   - Navigate to the `frontend` folder
   - Open `index.html` in a web browser
   - Or use a local server:
```bash
cd frontend
# Using Python
python -m http.server 8000
# Or using Node.js
npx http-server
```

2. **Access Application**
   - Open browser and go to `http://localhost:8000` (or the port your server uses)

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user

### Menu
- `GET /menu/all` - Get all menu items
- `GET /menu/available` - Get available items
- `GET /menu/category/{category}` - Get items by category
- `GET /menu/categories` - Get all categories
- `GET /menu/top-rated` - Get top-rated items
- `POST /menu/create` - Create new menu item (admin)

### Orders
- `POST /orders/create` - Create order
- `GET /orders/{orderId}` - Get order details
- `GET /orders/user/my-orders` - Get user's orders
- `PUT /orders/{orderId}/status` - Update order status
- `PUT /orders/{orderId}/cancel` - Cancel order

### Order Tracking
- `GET /tracking/{orderId}` - Get order tracking info
- `PUT /tracking/{orderId}` - Update tracking info

## Usage

### For Customers

1. **Register/Login**
   - Go to the application
   - Register a new account or login
   - You'll receive a JWT token for authentication

2. **Browse Menu**
   - View all available food items
   - Filter by category
   - Search for specific items

3. **Place Order**
   - Add items to cart
   - Click "Proceed to Checkout"
   - Enter delivery address and phone
   - Click "Place Order"

4. **Track Order**
   - Go to "My Orders"
   - Click "Track Order" on any order
   - View real-time order status and delivery info

## Configuration

### JWT Configuration
Edit `application.properties` to change JWT settings:
```properties
jwt.secret=ZippyRouteSecretKeyForJWTTokenGeneration2024
jwt.expiration=86400000  # 24 hours in milliseconds
```

### CORS Configuration
Update allowed origins in `application.properties`:
```properties
spring.web.cors.allowed-origins=http://localhost:3000,http://localhost:8081
```

### Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/zippyroute_db
spring.jpa.hibernate.ddl-auto=update  # Creates tables automatically
```

## Database Schema

### Users Table
- Stores user credentials and profile information
- Fields: id, email, password, fullName, phoneNumber, address, role, isActive, createdAt, updatedAt

### Menu Items Table
- Stores food items available for order
- Fields: id, name, description, price, category, imageUrl, rating, reviewCount, available, createdAt, updatedAt

### Orders Table
- Stores customer orders
- Fields: id, userId, deliveryAddress, phoneNumber, status, totalAmount, deliveryFee, discount, notes, createdAt, updatedAt

### Order Items Table
- Stores individual items in each order
- Fields: id, orderId, menuItemId, quantity, price, specialInstructions, createdAt

### Order Tracking Table
- Stores real-time tracking information
- Fields: id, orderId, currentStatus, latitude, longitude, deliveryPersonName, deliveryPersonPhone, estimatedDeliveryTime, statusHistory, createdAt, updatedAt

## Order Status Flow

```
PENDING → CONFIRMED → PREPARING → OUT_FOR_DELIVERY → DELIVERED
                                        ↓
                                   CANCELLED (at any point)
```

## Security Features

1. **Password Security**
   - Passwords are hashed using BCrypt
   - Never stored in plain text

2. **JWT Authentication**
   - All API endpoints (except auth) require valid JWT token
   - Token expires after 24 hours
   - Token should be sent as: `Authorization: Bearer <token>`

3. **CORS Protection**
   - Only allowed origins can access the API
   - Credentials validation enabled

## Troubleshooting

### Database Connection Error
- Ensure PostgreSQL is running
- Check database name, username, and password in `application.properties`
- Verify port 5432 is not blocked

### CORS Errors
- Update `spring.web.cors.allowed-origins` in `application.properties`
- Ensure frontend URL matches allowed origins

### Frontend Not Connecting to Backend
- Verify backend is running on `http://localhost:8080/api`
- Update `API_BASE_URL` in `frontend/js/app.js` if needed
- Check browser console for error messages

### JWT Token Errors
- Token might be expired - logout and login again
- Clear browser local storage and login again
- Check token format in Authorization header (should be "Bearer <token>")

## Future Enhancements

- [ ] Real-time notifications using WebSocket
- [ ] Payment integration (Stripe/PayPal)
- [ ] Admin dashboard
- [ ] Delivery partner app
- [ ] Push notifications
- [ ] Food ratings and reviews
- [ ] Promotional codes
- [ ] Multiple restaurant support
- [ ] Advanced analytics

## Performance Tips

1. **Frontend Optimization**
   - Cache menu items in localStorage
   - Use debouncing for search
   - Lazy load images

2. **Backend Optimization**
   - Add pagination to menu items endpoint
   - Index frequently queried fields in database
   - Implement caching layer

3. **Database Optimization**
   - Add indexes on userId, orderId, status
   - Archive old orders to separate table

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License

## Support

For issues and questions:
- Check the troubleshooting section
- Review API documentation
- Check browser console for errors
- Enable verbose logging in `application.properties`

---

**Happy Delivering! 🚗🍕**
