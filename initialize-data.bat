@echo off
REM ZippyRoute - Sample Data Initialization Script for Windows
REM This script adds sample menu items to the database

setlocal enabledelayedexpansion

set API_BASE_URL=http://localhost:8080/api

echo.
echo 🚀 Starting ZippyRoute Sample Data Initialization...
echo ==================================================

REM Sample menu items
set ITEMS=^
    {"name":"Margherita Pizza","description":"Fresh tomato basil and mozzarella","price":299,"category":"Pizza"}^
    {"name":"Pepperoni Pizza","description":"Loaded with pepperoni and cheese","price":349,"category":"Pizza"}^
    {"name":"Veggie Burger","description":"Fresh vegetables with wholegrain bun","price":199,"category":"Burgers"}^
    {"name":"Chicken Burger","description":"Grilled chicken breast with special sauce","price":229,"category":"Burgers"}^
    {"name":"Biryani","description":"Fragrant basmati rice with spices","price":249,"category":"Indian"}^
    {"name":"Butter Chicken","description":"Chicken in creamy tomato sauce","price":319,"category":"Indian"}^
    {"name":"Caesar Salad","description":"Crisp romaine with parmesan and croutons","price":179,"category":"Salads"}^
    {"name":"Greek Salad","description":"Feta cheese olives and tomatoes","price":189,"category":"Salads"}^
    {"name":"Chocolate Brownie","description":"Fudgy chocolate with nuts","price":99,"category":"Desserts"}^
    {"name":"Ice Cream Sundae","description":"Vanilla ice cream with toppings","price":129,"category":"Desserts"}^
    {"name":"Coca Cola","description":"Chilled cola beverage 250ml","price":49,"category":"Beverages"}^
    {"name":"Fresh Lemonade","description":"Freshly squeezed with ice","price":79,"category":"Beverages"}

echo ⏳ Waiting for API server to be ready...
timeout /t 2 /nobreak

echo.
echo Adding sample menu items...
echo.

REM Note: On Windows, you would need to download and use curl or use PowerShell
REM Using PowerShell for the API calls

powershell -Command ^
"$items = @( ^
    '{\"name\":\"Margherita Pizza\",\"description\":\"Fresh tomato basil and mozzarella\",\"price\":299,\"category\":\"Pizza\"}', ^
    '{\"name\":\"Pepperoni Pizza\",\"description\":\"Loaded with pepperoni and cheese\",\"price\":349,\"category\":\"Pizza\"}', ^
    '{\"name\":\"Veggie Burger\",\"description\":\"Fresh vegetables with wholegrain bun\",\"price\":199,\"category\":\"Burgers\"}', ^
    '{\"name\":\"Chicken Burger\",\"description\":\"Grilled chicken breast with special sauce\",\"price\":229,\"category\":\"Burgers\"}', ^
    '{\"name\":\"Biryani\",\"description\":\"Fragrant basmati rice with spices\",\"price\":249,\"category\":\"Indian\"}', ^
    '{\"name\":\"Butter Chicken\",\"description\":\"Chicken in creamy tomato sauce\",\"price\":319,\"category\":\"Indian\"}', ^
    '{\"name\":\"Caesar Salad\",\"description\":\"Crisp romaine with parmesan and croutons\",\"price\":179,\"category\":\"Salads\"}', ^
    '{\"name\":\"Greek Salad\",\"description\":\"Feta cheese olives and tomatoes\",\"price\":189,\"category\":\"Salads\"}', ^
    '{\"name\":\"Chocolate Brownie\",\"description\":\"Fudgy chocolate with nuts\",\"price\":99,\"category\":\"Desserts\"}', ^
    '{\"name\":\"Ice Cream Sundae\",\"description\":\"Vanilla ice cream with toppings\",\"price\":129,\"category\":\"Desserts\"}', ^
    '{\"name\":\"Coca Cola\",\"description\":\"Chilled cola beverage 250ml\",\"price\":49,\"category\":\"Beverages\"}', ^
    '{\"name\":\"Fresh Lemonade\",\"description\":\"Freshly squeezed with ice\",\"price\":79,\"category\":\"Beverages\"}' ^
); ^
foreach (\$item in \$items) { ^
    try { ^
        \$response = Invoke-WebRequest -Uri 'http://localhost:8080/api/menu/create' -Method POST -ContentType 'application/json' -Body \$item -UseBasicParsing; ^
        Write-Host \"✓ Added menu item successfully\"; ^
    } catch { ^
        Write-Host \"✗ Failed to add menu item\"; ^
    } ^
}"

echo.
echo ==================================================
echo ✅ Sample data initialization completed!
echo You can now login and browse the menu
echo ==================================================
